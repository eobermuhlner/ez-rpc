package ch.obermuhlner.rpc.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ch.obermuhlner.rpc.RpcException;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.transport.ClientTransport;
import ch.obermuhlner.rpc.transport.ServerTransport;
import ch.obermuhlner.rpc.transport.ServiceExecutor;

public class ServiceFactory {

	private static final String ASYNC_SUFFIX = "Async";

	private final MetaDataService metaDataService;
	
	private final ServiceExecutor localServiceExecutor;

	public ServiceFactory(MetaDataService metaDataService) {
		this.metaDataService = metaDataService;
		
		localServiceExecutor = new ServiceExecutor();
	}
	
	public <Service, AsyncService, ServiceImpl extends Service> Service createLocalService(Class<Service> serviceType, Class<AsyncService> asyncServiceType, ServiceImpl serviceImpl) {
		return createLocalService(serviceType, asyncServiceType, serviceImpl, () -> null, (session) -> {});
	}
	
	public <Service, AsyncService, ServiceImpl extends Service, Session> Service createLocalService(Class<Service> serviceType, Class<AsyncService> asyncServiceType, ServiceImpl serviceImpl, Supplier<Session> sessionSupplier, Consumer<Session> sessionConsumer) {
		Object proxyObject = Proxy.newProxyInstance(
				serviceType.getClassLoader(),
				new Class<?>[] { serviceType, asyncServiceType },
				(Object proxy, Method method, Object[] args) -> {
					try {
						if (method.getReturnType() == CompletableFuture.class || method.getReturnType() == Future.class) { 
							String requestId = UUID.randomUUID().toString();
							CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
								try {
									sessionConsumer.accept(sessionSupplier.get());
									String underlyingMethodName = withoutSuffix(method.getName(), ASYNC_SUFFIX);
									Method implMethod = serviceImpl.getClass().getMethod(underlyingMethodName, method.getParameterTypes());
									return localServiceExecutor.execute(requestId, serviceImpl, implMethod, args);
								} catch (Exception e) {
									throw new RpcException(e);
								} finally {
									sessionConsumer.accept(null);
								}
							});
							future.exceptionally((ex) -> {
								if (ex instanceof CancellationException) {
									localServiceExecutor.interruptRequestThread(requestId);
								}
								return null;
							});
							return future;
						} else {
							try {
								sessionConsumer.accept(sessionSupplier.get());
								Method implMethod = serviceImpl.getClass().getMethod(method.getName(), method.getParameterTypes());
								return implMethod.invoke(serviceImpl, args);
							} finally {
								sessionConsumer.accept(null);
							}
						}
					} catch (InvocationTargetException e) {
						throw e.getTargetException();
					}
				});
		
		@SuppressWarnings("unchecked")
		Service proxyService = (Service) proxyObject;
		
		return proxyService;
	}

	public <Service, AsyncService> Service createRemoteService(Class<Service> serviceType, ClientTransport clientTransport) {
		return createRemoteService(serviceType, null, clientTransport, () -> null);
	}
	
	public <Service, AsyncService> Service createRemoteService(Class<Service> serviceType, Class<AsyncService> asyncServiceType, ClientTransport clientTransport) {
		return createRemoteService(serviceType, asyncServiceType, clientTransport, () -> null);
	}
	
	public <Service, AsyncService, Session> Service createRemoteService(Class<Service> serviceType, Class<AsyncService> asyncServiceType, ClientTransport clientTransport, Supplier<Session> sessionSupplier) {
		Class<?>[] interfaces = asyncServiceType == null ? new Class<?>[] { serviceType } : new Class<?>[] { serviceType, asyncServiceType };
		
		Object proxyObject = Proxy.newProxyInstance(
				serviceType.getClassLoader(),
				interfaces,
				(Object proxy, Method method, Object[] args) -> {
					try {
						boolean asyncMode = method.getReturnType() == CompletableFuture.class || method.getReturnType() == Future.class;
						
						String serviceName = metaDataService.registerService(serviceType).name;
						String methodName = method.getName(); // TODO ask metaDataService for method name
						if (asyncMode) {
							methodName = withoutSuffix(methodName, ASYNC_SUFFIX); 
						}
	
						Request request = new Request();
						request.serviceName = serviceName;
						request.methodName = methodName;
						request.execute = true;
						request.arguments = metaDataService.createDynamicStruct(method, args);
						request.session = sessionSupplier.get();
						request.requestId = UUID.randomUUID().toString();
						CompletableFuture<Object> future = clientTransport.send(request)
								.thenApply(response -> {
									if (response.exception != null) {
										throwAsException(metaDataService.adaptRemoteToLocal(response.exception));
									}
									Object result = response.result.getField("result");
									return result;
								});
						future.exceptionally((ex) -> {
							if (ex instanceof CancellationException) {
								Request cancelRequest = new Request();
								cancelRequest.serviceName = request.serviceName;
								cancelRequest.methodName = request.methodName;
								cancelRequest.execute = false;
								cancelRequest.requestId = request.requestId;
								clientTransport.send(cancelRequest);
							}
							return null;
						});
						if (asyncMode) {
							return future;
						} else {
							return future.get();
						}
					} catch (ExecutionException e) {
						throw e.getCause();
					}
				});
		
		@SuppressWarnings("unchecked")
		Service proxyService = (Service) proxyObject;
		
		return proxyService;
	}

	private void throwAsException(Object exception) {
		if (exception == null) {
			return;
		}
		
		if (exception instanceof RuntimeException) {
			throw (RuntimeException) exception;
		}
		
		if (exception instanceof Error) {
			throw (Error) exception;
		}
		
		if (exception instanceof Exception) {
			throw new RpcException((Exception) exception);
		}
	}

	public <Service, ServiceImpl extends Service> void publishService(Class<Service> serviceType, ServiceImpl serviceImpl, ServerTransport serverTransport) {
		publishService(serviceType, serviceImpl, serverTransport, (session) -> {});
	}
	
	public <Service, ServiceImpl extends Service, Session> void publishService(Class<Service> serviceType, ServiceImpl serviceImpl, ServerTransport serverTransport, Consumer<Session> sessionConsumer) {
		serverTransport.register(serviceType, serviceImpl, sessionConsumer);
	}

	private String withoutSuffix(String name, String suffix) {
		if (name.endsWith(suffix)) {
			return name.substring(0, name.length() - suffix.length());
		}
		
		return name;
	}
}
