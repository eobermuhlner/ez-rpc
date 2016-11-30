package ch.obermuhlner.rpc.protocol;

import static org.junit.Assert.assertEquals;

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
}
