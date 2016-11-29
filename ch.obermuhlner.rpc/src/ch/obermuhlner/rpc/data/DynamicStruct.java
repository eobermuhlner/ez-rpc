package ch.obermuhlner.rpc.data;

import java.util.HashMap;
import java.util.Map;

public class DynamicStruct {
	
	public String name;

	public final Map<String, Object> fields = new HashMap<>();

	@Override
	public String toString() {
		return "DynamicStruct [name=" + name + ", fields=" + fields + "]";
	}
}
