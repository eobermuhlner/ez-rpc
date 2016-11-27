package ch.obermuhlner.rpc.example.api;

import java.math.BigDecimal;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "AdapterExampleData")
public class AdapterExampleData {
	public BigDecimal bigDecimalField;

	@Override
	public String toString() {
		return "AdapterExampleData [bigDecimalField=" + bigDecimalField + "]";
	}
}
