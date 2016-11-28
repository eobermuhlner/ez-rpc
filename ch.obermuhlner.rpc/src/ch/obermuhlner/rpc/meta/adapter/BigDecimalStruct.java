package ch.obermuhlner.rpc.meta.adapter;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "BigDecimal")
public class BigDecimalStruct {
	public String value;
}