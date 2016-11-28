package ch.obermuhlner.rpc.meta.adapter;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "Date")
public class DateStruct {
	public long milliseconds;
}