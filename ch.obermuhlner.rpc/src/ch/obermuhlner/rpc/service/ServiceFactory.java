package ch.obermuhlner.rpc.service;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import ch.obermuhlner.rpc.RpcServiceException;
import ch.obermuhlner.rpc.transport.ClientTransport;
import ch.obermuhlner.rpc.transport.ServerTransport;

public class ServiceFactory {

	private static final String ASYNC_SUFFIX = "Async";

	public <Service, AsyncService, ServiceImpl extends Service> Service createLocalService(Class<Service> serviceType, Class<AsyncService> asyncServiceType, ServiceImpl serviceImpl) {
		Object proxyObject = Proxy.newProxyInstance(
				serviceType.getClassLoader(),
				new Class<?>[] { serviceType, asyncServiceType },
				(Object proxy, Method method, Object[] args) -> {
					if (method.getReturnType() == CompletableFuture.class || method.getReturnType() == Future.class) { 
						return CompletableFuture.supplyAsync(() -> {
							String syncMethodName = withoutAsyncSuffix(method.getName());
							Method implMethod;
							try {
								implMethod = serviceImpl.getClass().getMethod(syncMethodName, method.getParameterTypes());
								return implMethod.invoke(serviceImpl, args);
							} catch (Exception e) {
								throw new RpcServiceException(e);
							}
						});
					} else {
						Method implMethod = serviceImpl.getClass().getMethod(method.getName(), method.getParameterTypes());
						return implMethod.invoke(serviceImpl, args);
					}
				});
		
		@SuppressWarnings("unchecked")
		Service proxyService = (Service) proxyObject;
		
		return proxyService;
	}

	public <Service, AsyncService> Service createRemoteService(Class<Service> serviceType, Class<AsyncService> asyncServiceType, ClientTransport clientTransport) {
		Object proxyObject = Proxy.newProxyInstance(
				serviceType.getClassLoader(),
				new Class<?>[] { serviceType, asyncServiceType },
				(Object proxy, Method method, Object[] args) -> {
					boolean async = method.getReturnType() == CompletableFuture.class || method.getReturnType() == Future.class;
					
					String serviceName = serviceType.getName();
					String methodName = async ? withoutAsyncSuffix(method.getName()) : method.getName();

					Request request = new Request();
					request.serviceName = serviceName;
					request.methodName = methodName;
					request.arguments = args;
					CompletableFuture<Object> future = clientTransport.send(request)
							.thenApply(response -> response.result);
					if (async) {
						return future;
					} else {
						return future.get();
					}
				});
		
		@SuppressWarnings("unchecked")
		Service proxyService = (Service) proxyObject;
		
		return proxyService;
	}
	
	public <Service, ServiceImpl extends Service> void publishService(Class<Service> serviceType, ServiceImpl serviceImpl, ServerTransport serverTransport) {
		serverTransport.register(serviceType, serviceImpl);
	}

	private String withoutAsyncSuffix(String name) {
		if (name.endsWith(ASYNC_SUFFIX)) {
			return name.substring(0, name.length() - ASYNC_SUFFIX.length());
		}
		
		return name;
	}
}
