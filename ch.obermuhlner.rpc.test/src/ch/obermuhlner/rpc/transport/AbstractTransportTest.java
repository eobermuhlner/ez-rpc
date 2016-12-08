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
	
	@Test
	public void testSleep() {
		long slept = testService.sleep(1000);
		assertEquals(true, slept > 900);
	}
	
	@Test
	public void testSleepAsync() throws InterruptedException, ExecutionException {
		CompletableFuture<Long> future = testServiceAsync.sleepAsync(1000);
		assertEquals(true, future.get() > 900);
		
		assertEquals(false, testService.lastSleepWasInterrupted());
	}
	
	@Test
	public void testSleepAsyncCancel() throws InterruptedException, ExecutionException {
		CompletableFuture<Long> future = testServiceAsync.sleepAsync(1000);
		future.cancel(true);
		assertEquals(true, future.isCancelled());

		assertEquals(true, testService.lastSleepWasInterrupted());
		assertEquals(false, Thread.currentThread().isInterrupted());
	}
	
	@RpcService
	public static interface TestService {
		void methodVoidToVoid();
		int methodVoidToInt();
		int methodIntToInt(int value);
		
		int methodIllegalArgumentException();

		long sleep(long milliseconds);
		
		boolean lastSleepWasInterrupted();
	}

	public static interface TestServiceAsync {
		CompletableFuture<Integer> methodVoidToIntAsync();
		Future<Integer> methodIntToIntAsync(int value);
		CompletableFuture<Long> sleepAsync(long milliseconds);
	}

	public static class TestServiceImpl implements TestService {
		/**
		 * Don't do this at home!
		 * Services should always be stateless and not depend on previous calls.
		 * In this case we do this for testing purposes.
		 */
		private volatile boolean lastSleepInterrupted;
		
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
		
		@Override
		public long sleep(long milliseconds) {
			lastSleepInterrupted = false;
			long startMillis = System.currentTimeMillis();
			try {
				Thread.sleep(milliseconds);
			} catch (InterruptedException e) {
				lastSleepInterrupted = true;
			}
			long endMillis = System.currentTimeMillis();
			return endMillis - startMillis;
		}

		@Override
		public boolean lastSleepWasInterrupted() {
			return lastSleepInterrupted;
		}
	}
}
