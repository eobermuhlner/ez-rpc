package ch.obermuhlner.rpc.meta.adapter.time;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct()
public class DateStruct {
	public int year;
	public int month;
	public int day;
}