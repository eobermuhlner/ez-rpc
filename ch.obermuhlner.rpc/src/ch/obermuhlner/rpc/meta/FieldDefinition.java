package ch.obermuhlner.rpc.meta;

public class FieldDefinition {

	public String name;
	
	public Type type;
	
	public FieldDefinition() {
		// for Jaxb
	}

	public FieldDefinition(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return "FieldDefinition [name=" + name + ", type=" + type + "]";
	}
}
