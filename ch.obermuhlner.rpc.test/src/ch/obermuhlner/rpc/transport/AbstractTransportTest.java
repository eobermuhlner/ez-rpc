package ch.obermuhlner.rpc.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.obermuhlner.rpc.annotation.RpcService;

public abstract class AbstractTransportTest {

	protected abstract TestService getTestService();

	@Test
	public void testService() {
		TestService testService = getTestService();
		
		assertEquals(1, testService.testMethod());
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
