package ch.obermuhlner.rpc.transport.local;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import ch.obermuhlner.rpc.annotation.RpcService;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.protocol.java.JavaSerializableProtocol;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public class LocalTransportTest {

	@Test
	public void testBasics() throws InterruptedException, ExecutionException {
		MetaDataService metaDataService = new MetaDataService();
		metaDataService.registerService(TestService.class);
		
		Protocol<Object> protocol = new JavaSerializableProtocol(getClass().getClassLoader());
		LocalTransport localTransport = new LocalTransport(metaDataService, protocol);
		localTransport.register(TestService.class, new TestServiceImpl(), (session) -> {});
		
		Request request = new Request();
		request.serviceName = "TestService";
		request.methodName = "testMethod";
		
		CompletableFuture<Response> future = localTransport.send(request);
		Response response = future.get();
		
		assertEquals(1, response.result.getField("result"));
	}
	
	@RpcService
	public static interface TestService {
		int testMethod();
	}
	
	public static class TestServiceImpl implements TestService {
		@Override
		public int testMethod() {
			return 1;
		}
		
	}
}
