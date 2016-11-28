package ch.obermuhlner.rpc.meta.adapter.bigdecimal;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "BigDecimal")
public class BigDecimalStruct {
	public String value;
}