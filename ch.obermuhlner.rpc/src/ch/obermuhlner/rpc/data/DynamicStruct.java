package ch.obermuhlner.rpc.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DynamicStruct {
	
	public String name;

	private final Map<String, Object> fields = new HashMap<>();

	public void setField(String name, Object value) {
		fields.put(name, value);
	}

	public Object getField(String name) {
		return fields.get(name);
	}
	
	@Override
	public String toString() {
		return "DynamicStruct [name=" + name + ", fields=" + fields + "]";
	}

	public Set<String> getFields() {
		return fields.keySet();
	}
}
