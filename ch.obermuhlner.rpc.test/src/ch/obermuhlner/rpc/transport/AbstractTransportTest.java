package ch.obermuhlner.rpc.transport;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;

import ch.obermuhlner.rpc.annotation.RpcService;

public abstract class AbstractTransportTest {

	protected static TestService testService;

	protected static TestServiceAsync testServiceAsync;

	@Test
	public void testService() {
		testService.methodVoidToInt();
		assertEquals(1, testService.methodVoidToInt());
		assertEquals(2, testService.methodIntToInt(1));
	}
	
	@Test
	public void testAsync() throws InterruptedException, ExecutionException {
		CompletableFuture<Integer> futureVoidToInt = testServiceAsync.methodVoidToIntAsync();
		Future<Integer> futureIntToInt = testServiceAsync.methodIntToIntAsync(1);

		assertEquals(Integer.valueOf(1), futureVoidToInt.get());
		assertEquals(Integer.valueOf(2), futureIntToInt.get());
	}
	
	@RpcService
	public static interface TestService {
		void methodVoidToVoid();
		int methodVoidToInt();
		int methodIntToInt(int value);
	}

	public static interface TestServiceAsync {
		CompletableFuture<Integer> methodVoidToIntAsync();
		Future<Integer> methodIntToIntAsync(int value);
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
