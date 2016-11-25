package ch.obermuhlner.rpc.transport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import ch.obermuhlner.rpc.RpcServiceException;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public class LocalTransport implements Transport {

	private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
	private final Map<String, Method> methodMap = new ConcurrentHashMap<>();
	
	public <Service> void register(Class<Service> serviceType, Service service) {
		String serviceName = serviceType.getName();
		
		serviceMap.put(serviceName, service);
		
		for (Method method : serviceType.getMethods()) {
			String methodName = method.getName();
			
			String key = serviceName + "#" + methodName;
			methodMap.put(key, method);
		}
	}
	
	@Override
	public CompletableFuture<Response> send(Request request) {
		return CompletableFuture.supplyAsync(() -> receive(request));
	}

	@Override
	public Response receive(Request request) {
		if (!serviceMap.containsKey(request.serviceName)) {
			throw new RpcServiceException("No registered service: " + request.serviceName);
		}

		Object service = serviceMap.get(request.serviceName);
		
		String key = request.serviceName + "#" + request.methodName;
		if (!methodMap.containsKey(key)) {
			throw new RpcServiceException("No registered service method: " + key);
		}
		
		Method method = methodMap.get(key);
		
		Response response = new Response();
		try {
			response.result = method.invoke(service, request.arguments);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new RpcServiceException(e);
		} catch (InvocationTargetException e) {
			response.exception = e;
		}
		
		return response;
	}

}
