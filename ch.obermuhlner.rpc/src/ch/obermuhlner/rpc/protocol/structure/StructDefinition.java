package ch.obermuhlner.rpc.protocol.structure;

public class StructDefinition {

	public String name;
	
	public Class<?> type;

	public StructDefinition(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return "StructDefinition [name=" + name + ", type=" + type + "]";
	}
}
