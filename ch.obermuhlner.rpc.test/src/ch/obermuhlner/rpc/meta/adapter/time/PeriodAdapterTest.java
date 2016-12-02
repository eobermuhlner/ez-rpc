package ch.obermuhlner.rpc.meta.adapter.time;

import static org.junit.Assert.assertEquals;

import java.time.Period;

import org.junit.Test;

public class PeriodAdapterTest {

	@Test
	public void testTypes() {
		PeriodAdapter adapter = new PeriodAdapter();
		
		assertEquals(Period.class, adapter.getLocalType());
		assertEquals(PeriodStruct.class, adapter.getRemoteType());
	}
	
	@Test
	public void testConvertLocalToRemote() {
		PeriodAdapter adapter = new PeriodAdapter();
		
		assertEquals(3, adapter.convertLocalToRemote(Period.ofYears(3)).years);
		assertEquals(0, adapter.convertLocalToRemote(Period.ofYears(3)).months);
		assertEquals(0, adapter.convertLocalToRemote(Period.ofYears(3)).days);
		
		assertEquals(0, adapter.convertLocalToRemote(Period.ofMonths(4)).years);
		assertEquals(4, adapter.convertLocalToRemote(Period.ofMonths(4)).months);
		assertEquals(0, adapter.convertLocalToRemote(Period.ofMonths(4)).days);
		
		assertEquals(0, adapter.convertLocalToRemote(Period.ofDays(5)).years);
		assertEquals(0, adapter.convertLocalToRemote(Period.ofDays(5)).months);
		assertEquals(5, adapter.convertLocalToRemote(Period.ofDays(5)).days);
	}
	
	@Test
	public void testConvertRemoteToLocal() {
		PeriodAdapter adapter = new PeriodAdapter();
		
		PeriodStruct periodStruct = new PeriodStruct();
		periodStruct.years = 3;
		periodStruct.months = 2;
		periodStruct.days = 1;
		assertEquals(Period.of(3, 2, 1), adapter.convertRemoteToLocal(periodStruct));
	}
}
