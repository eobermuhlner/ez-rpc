package ch.obermuhlner.rpc.service;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import ch.obermuhlner.rpc.RpcServiceException;

public class ServiceFactory {

	private static final String ASYNC_SUFFIX = "Async";

	public static <Service, AsyncService, ServiceImpl extends Service> Service createLocalService(Class<Service> serviceType, Class<AsyncService> asyncServiceType, ServiceImpl serviceImpl) {
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
					}
					
					Method implMethod = serviceImpl.getClass().getMethod(method.getName(), method.getParameterTypes());
					return implMethod.invoke(serviceImpl, args);
				});
		
		@SuppressWarnings("unchecked")
		Service proxyService = (Service) proxyObject;
		
		return proxyService;
	}

	private static String withoutAsyncSuffix(String name) {
		if (name.endsWith(ASYNC_SUFFIX)) {
			return name.substring(0, name.length() - ASYNC_SUFFIX.length());
		}
		
		return name;
	}
}
