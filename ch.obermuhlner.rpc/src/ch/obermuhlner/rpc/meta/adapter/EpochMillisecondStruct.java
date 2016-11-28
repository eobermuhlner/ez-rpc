package ch.obermuhlner.rpc.meta.adapter;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "EpochMillisecond")
public class EpochMillisecondStruct {
	public long milliseconds;
}