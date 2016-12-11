package ch.obermuhlner.rpc.transport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import ch.obermuhlner.rpc.RpcException;
import ch.obermuhlner.rpc.data.DynamicStruct;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public class ServerTransportImpl extends ServiceExecutor implements ServerTransport {

	private final MetaDataService metaDataService;
	
	private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
	private final Map<String, Method> methodMap = new ConcurrentHashMap<>();
	private final Map<String, Consumer<?>> serviceToSessionConsumerMap = new ConcurrentHashMap<>();
	
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
		if (!request.execute) {
			return cancelRequest(request);
		}
		
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
		Object[] arguments = metaDataService.toArguments(method, request.arguments);
		
		Response response = new Response();
		try {
			sessionConsumer.accept(request.session);
			Object result = execute(request.requestId, service, method, arguments);
			response.result = new DynamicStruct();
			response.result.name = method + "_Reponse";
			response.result.setField("result", result);
			sessionConsumer.accept(null);
		} catch (InvocationTargetException e) {
			response.exception = e.getTargetException();
		}
		
		return response;
	}

	private Response cancelRequest(Request request) {
		interruptRequestThread(request.requestId);

		Response response = new Response();
		return response;
	}

}
