package ch.obermuhlner.rpc.meta.adapter.time;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "Period")
public class PeriodStruct {
	public int years;
	public int months;
	public int days;
}
