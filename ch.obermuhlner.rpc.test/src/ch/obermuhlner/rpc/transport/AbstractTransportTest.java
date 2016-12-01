package ch.obermuhlner.rpc.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.obermuhlner.rpc.annotation.RpcService;

public abstract class AbstractTransportTest {

	protected abstract TestService getTestService();

	@Test
	public void testService() {
		TestService testService = getTestService();
		
		testService.methodVoidToInt();
		assertEquals(1, testService.methodVoidToInt());
		assertEquals(2, testService.methodIntToInt(1));
	}
	
	@RpcService
	public static interface TestService {
		void methodVoidToVoid();
		int methodVoidToInt();
		int methodIntToInt(int value);
	}
	
	public static class TestServiceImpl implements TestService {
		@Override
		public void methodVoidToVoid() {
		}

		@Override
		public int methodVoidToInt() {
			return 1;
		}

		@Override
		public int methodIntToInt(int value) {
			return 2;
		}
		
	}
}
