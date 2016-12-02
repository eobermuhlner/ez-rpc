package ch.obermuhlner.rpc.meta.adapter.bigdecimal;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalAdapterTest {

	@Test
	public void testBasics() {
		BigDecimalAdapter adapter = new BigDecimalAdapter();
		
		assertEquals(BigDecimal.class, adapter.getLocalType());
		assertEquals(BigDecimalStruct.class, adapter.getRemoteType());
	}
	
	@Test
	public void testConvertLocalToRemote() {
		BigDecimalAdapter adapter = new BigDecimalAdapter();
		
		assertEquals("3.14", adapter.convertLocalToRemote(new BigDecimal("3.14")).value);
	}
	
	@Test
	public void testConvertRemoteToLocal() {
		BigDecimalAdapter adapter = new BigDecimalAdapter();
		
		BigDecimalStruct bigDecimalStruct = new BigDecimalStruct();
		bigDecimalStruct.value = "123.456";
		assertEquals(new BigDecimal("123.456"), adapter.convertRemoteToLocal(bigDecimalStruct));
	}
}
