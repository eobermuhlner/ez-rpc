package ch.obermuhlner.rpc.example.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "ExampleData")
public class ExampleData {
	public boolean booleanField;
	public int intField;
	public long longField;
	public String stringField;
	public List<String> listField;
	public Set<String> setField;
	public Map<Object, Object> mapField;
	public ExampleData nestedExampleData;

	@Override
	public String toString() {
		return "ExampleData [booleanField=" + booleanField + ", intField=" + intField + ", longField=" + longField
				+ ", stringField=" + stringField + ", listField=" + listField + ", setField=" + setField + ", mapField="
				+ mapField + ", nestedExampleData=" + nestedExampleData + "]";
	}
}