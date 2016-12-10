package ch.obermuhlner.rpc.transport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import ch.obermuhlner.rpc.RpcAlreadyCancelledException;
import ch.obermuhlner.rpc.RpcException;
import ch.obermuhlner.rpc.data.DynamicStruct;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.service.CancelRequest;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public class ServerTransportImpl implements ServerTransport {

	private final MetaDataService metaDataService;
	
	private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
	private final Map<String, Method> methodMap = new ConcurrentHashMap<>();
	private final Map<String, Consumer<?>> serviceToSessionConsumerMap = new ConcurrentHashMap<>();
	
	private final Map<String, Thread> requestIdToThreadMap = new HashMap<>();
	
	public ServerTransportImpl(MetaDataService metaDataService) {
		this.metaDataService = metaDataService;
	}
	
	@Override
	public <Service, Session> void register(Class<Service> serviceType, Service service, Consumer<Session> sessionConsumer) {
		String serviceName = metaDataService.registerService(serviceType).name;
		
		serviceMap.put(serviceName, service);
		serviceToSessionConsumerMap.put(serviceName, sessionConsumer);
		
		for (Method method : serviceType.getMethods()) {
			String methodName = method.getName();
			
			String key = serviceName + "#" + methodName;
			methodMap.put(key, method);
		}
	}
	
	@Override
	public Response receive(Request request) {
		if (request.serviceName == null) {
			throw new RpcException("No service name in request: " + request);
		}
		if (request.methodName == null) {
			throw new RpcException("No method name in request: " + request);
		}
		if (!serviceMap.containsKey(request.serviceName)) {
			throw new RpcException("No registered service: " + request.serviceName);
		}

		Object service = serviceMap.get(request.serviceName);

		@SuppressWarnings("unchecked")
		Consumer<Object> sessionConsumer = (Consumer<Object>) serviceToSessionConsumerMap.get(request.serviceName);

		String key = request.serviceName + "#" + request.methodName;
		if (!methodMap.containsKey(key)) {
			throw new RpcException("No registered service method: " + key);
		}
		
		Method method = methodMap.get(key);
		
		Response response = new Response();
		try {
			startRequestThread(request.requestId);
			sessionConsumer.accept(request.session);
			Object result = method.invoke(service, metaDataService.toArguments(method, request.arguments));
			response.result = new DynamicStruct();
			response.result.name = method + "_Reponse";
			response.result.setField("result", result);
			sessionConsumer.accept(null);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new RpcException(e);
		} catch (InvocationTargetException e) {
			response.exception = e.getTargetException();
		} catch (RpcAlreadyCancelledException e) {
			// ignored - since this is a valid special case - request already cancelled before being executed
		} finally {
			finishRequestThread(request.requestId);
		}
		
		return response;
	}

	@Override
	public void receiveCancel(CancelRequest cancelRequest) {
		interruptRequestThread(cancelRequest.requestId);
	}
	
	private void startRequestThread(String requestId) {
		synchronized (requestIdToThreadMap) {
			if (requestIdToThreadMap.containsKey(requestId)) {
				Thread thread = requestIdToThreadMap.get(requestId);
				if (thread == null) {
					throw new RpcAlreadyCancelledException("Request already cancelled: " + requestId);
				} else {
					throw new IllegalStateException("Request already executed in a thread: " + requestId + " " + thread);
				}
			}
			requestIdToThreadMap.put(requestId, Thread.currentThread());
		}		
	}

	private void finishRequestThread(String requestId) {
		synchronized (requestIdToThreadMap) {
			requestIdToThreadMap.remove(requestId);
			Thread.interrupted(); // clear interrupted flag
		}
	}

	private void interruptRequestThread(String requestId) {
		synchronized (requestIdToThreadMap) {
			Thread thread = requestIdToThreadMap.get(requestId);
			if (thread == null) {
				// special case - cancel request arrived before execution request
				requestIdToThreadMap.put(requestId, null);
			} else {
				thread.interrupt();
			}
		}
	}
}
