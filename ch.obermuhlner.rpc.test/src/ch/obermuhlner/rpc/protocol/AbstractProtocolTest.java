package ch.obermuhlner.rpc.protocol;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import ch.obermuhlner.rpc.protocol.Protocol;

public abstract class AbstractProtocolTest {
	
	private Protocol<Object> protocol;

	protected abstract <T> Protocol<T> getProtocol();

	@Before
	public void setup() {
		protocol = getProtocol();
	}
	
	@Test
	public void testNull() {
		assertEquals(null, protocol.deserializeFromBytes(protocol.serializeToBytes(null)));
	}
	
	@Test
	public void testInteger() {
		assertEquals(123, protocol.deserializeFromBytes(protocol.serializeToBytes(123)));
	}
	
	@Test
	public void testLong() {
		assertEquals(1234567L, protocol.deserializeFromBytes(protocol.serializeToBytes(1234567L)));
	}
	
	@Test
	public void testDouble() {
		assertEquals(3.1415926, protocol.deserializeFromBytes(protocol.serializeToBytes(3.1415926)));
	}
	
	@Test
	public void testString() {
		assertEquals("", protocol.deserializeFromBytes(protocol.serializeToBytes("")));
		assertEquals("Hello", protocol.deserializeFromBytes(protocol.serializeToBytes("Hello")));
	}
	
	@Test
	public void testList() {
		assertEquals(Arrays.asList(), protocol.deserializeFromBytes(protocol.serializeToBytes(Arrays.asList())));
		assertEquals(Arrays.asList("one", "two"), protocol.deserializeFromBytes(protocol.serializeToBytes(Arrays.asList("one", "two"))));
	}
	
	@Test
	public void testSet() {
		assertEquals(new HashSet<>(), protocol.deserializeFromBytes(protocol.serializeToBytes(new HashSet<>())));
		assertEquals(new HashSet<>(Arrays.asList("one", "two")), protocol.deserializeFromBytes(protocol.serializeToBytes(new HashSet<>(Arrays.asList("one", "two")))));
	}
	
	@Test
	public void testMap() {
		assertEquals(new HashMap<>(), protocol.deserializeFromBytes(protocol.serializeToBytes(new HashMap<>())));

		HashMap<Integer, String> testMap = new HashMap<>();
		testMap.put(1, "one");
		testMap.put(2, "two");
		assertEquals(testMap, protocol.deserializeFromBytes(protocol.serializeToBytes(testMap)));
	}
}
