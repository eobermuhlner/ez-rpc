package ch.obermuhlner.rpc.meta.adapter.exception;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "IllegalArgument")
public class IllegalArgumentStruct {

	public String message;
}
