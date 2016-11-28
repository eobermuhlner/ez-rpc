package ch.obermuhlner.rpc.example.api;

import java.math.BigDecimal;
import java.util.Date;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "AdapterExampleData")
public class AdapterExampleData {
	public BigDecimal bigDecimalField;
	public Date dateField;

	@Override
	public String toString() {
		return "AdapterExampleData [bigDecimalField=" + bigDecimalField + ", dateField=" + dateField + "]";
	}
}
