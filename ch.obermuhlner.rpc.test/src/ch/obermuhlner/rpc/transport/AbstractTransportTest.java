package ch.obermuhlner.rpc.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import ch.obermuhlner.rpc.annotation.RpcService;

public abstract class AbstractTransportTest {

	private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "This is an expected exception";

	protected static TestService testService;

	protected static TestServiceAsync testServiceAsync;

	@Test
	public void testSync() {
		testService.methodVoidToVoid();
		assertEquals(1, testService.methodVoidToInt());
		assertEquals(2, testService.methodIntToInt(1));
		assertEquals("int:1234", testService.methodIntToString(1234));
		assertEquals(Arrays.asList("first", "int:123", "last"), testService.methodIntToListOfString(123));
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
		int sleepCounter = testService.sleepCounter();
		
		CompletableFuture<Long> future = testServiceAsync.sleepAsync(1000);
		assertEquals(true, future.get() > 900);
		
		assertEquals(sleepCounter + 1, testService.sleepCounter());
	}
	
	@Test
	public void testSleepAsyncCancel() throws InterruptedException, ExecutionException {
		int sleepCounter = testService.sleepCounter();

		CompletableFuture<Long> future = testServiceAsync.sleepAsync(1000);
		future.cancel(true);
		assertEquals(true, future.isCancelled());

		Thread.sleep(2000); // wait to make sure we catch the incremented sleep counter in case the service execution is not interrupted
		
		assertEquals(sleepCounter + 0, testService.sleepCounter());
		assertEquals(false, Thread.currentThread().isInterrupted());
	}
	
	@RpcService
	public static interface TestService {
		void methodVoidToVoid();
		int methodVoidToInt();
		int methodIntToInt(int value);
		String methodIntToString(int value);
		List<String> methodIntToListOfString(int value);
		
		int methodIllegalArgumentException();

		long sleep(long milliseconds);
		
		int sleepCounter();
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
		private AtomicInteger sleepCounter = new AtomicInteger();
		
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
		public String methodIntToString(int value) {
			return "int:" + String.valueOf(value);
		}
		
		@Override
		public List<String> methodIntToListOfString(int value) {
			return Arrays.asList("first", "int:" + value , "last");
		}

		@Override
		public int methodIllegalArgumentException() {
			throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
		}
		
		@Override
		public long sleep(long milliseconds) {
			long startMillis = System.currentTimeMillis();
			try {
				Thread.sleep(milliseconds);
				long endMillis = System.currentTimeMillis();
				long deltaMillis = endMillis - startMillis;
				sleepCounter.incrementAndGet();
				return deltaMillis;
			} catch (InterruptedException e) {
				return -1;
			}
		}
		
		@Override
		public int sleepCounter() {
			return sleepCounter.get();
		}
	}
}
