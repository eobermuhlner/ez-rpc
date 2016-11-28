package ch.obermuhlner.rpc.meta.adapter.time;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "EpochDay")
public class EpochDayStruct {
	public long days;
}