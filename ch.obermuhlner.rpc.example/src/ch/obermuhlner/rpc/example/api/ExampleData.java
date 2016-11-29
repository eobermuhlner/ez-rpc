package ch.obermuhlner.rpc.example.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.obermuhlner.rpc.annotation.RpcField;
import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct()
public class ExampleData {
	public boolean booleanField;
	public int intField;
	public long longField;
	public String stringField;
	@RpcField(element=String.class)
	public List<String> listField;
	@RpcField(element=String.class)
	public Set<String> setField;
	@RpcField(key=Integer.class, value=String.class)
	public Map<Integer, String> mapField;
	public ExampleData nestedExampleData;

	@Override
	public String toString() {
		return "ExampleData [booleanField=" + booleanField + ", intField=" + intField + ", longField=" + longField
				+ ", stringField=" + stringField + ", listField=" + listField + ", setField=" + setField + ", mapField="
				+ mapField + ", nestedExampleData=" + nestedExampleData + "]";
	}
}
