package ch.obermuhlner.rpc.transport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import ch.obermuhlner.rpc.exception.RpcAlreadyCancelledException;
import ch.obermuhlner.rpc.exception.RpcException;

public class ServiceExecutor {

	private final Map<String, Thread> requestIdToThreadMap = new HashMap<>();

	public Object execute(String requestId, Object service, Method method, Object[] args) throws InvocationTargetException {
		Object result = null;
		
		try {
			startRequestThread(requestId);
			result = method.invoke(service, args);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new RpcException(e);
		} catch (RpcAlreadyCancelledException e) {
			// ignored - since this is a valid special case - request already cancelled before being executed
		} finally {
			finishRequestThread(requestId);
		}
		
		return result;
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

	public void interruptRequestThread(String requestId) {
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
