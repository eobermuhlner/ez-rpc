package ch.obermuhlner.rpc.example.api;

import java.util.List;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "ExampleData")
public class ExampleData {
	public int intField;
	public long longField;
	public String stringField;
	public List<String> listField;
	public ExampleData nestedExampleData;
	
	@Override
	public String toString() {
		return "ExampleData [intField=" + intField + ", longField=" + longField + ", stringField=" + stringField
				+ ", listField=" + listField + ", nestedExampleData=" + nestedExampleData + "]";
	}
}
