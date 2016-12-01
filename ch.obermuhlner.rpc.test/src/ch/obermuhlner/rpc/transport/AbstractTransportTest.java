package ch.obermuhlner.rpc.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;

import ch.obermuhlner.rpc.annotation.RpcService;

public abstract class AbstractTransportTest {

	private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "This is an expected exception";

	protected static TestService testService;

	protected static TestServiceAsync testServiceAsync;

	@Test
	public void testSync() {
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
	
	@Test
	public void testThrowIllegalArgumentException() {
		try {
			testService.methodIllegalArgumentException();
			fail("Expected an exception");
		} catch(IllegalArgumentException e) {
			assertEquals(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, e.getMessage());
		}
	}
	
	@RpcService
	public static interface TestService {
		void methodVoidToVoid();
		int methodVoidToInt();
		int methodIntToInt(int value);
		
		int methodIllegalArgumentException();

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

		@Override
		public int methodIllegalArgumentException() {
			throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
		}
	}
}
