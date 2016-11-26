package ch.obermuhlner.rpc.meta;

public class StructDefinition {

	public String name;
	
	public String javaTypeName;

	public StructDefinition(String name, String javaTypeName) {
		this.name = name;
		this.javaTypeName = javaTypeName;
	}

	@Override
	public String toString() {
		return "StructDefinition [name=" + name + ", javaTypeName=" + javaTypeName + "]";
	}
}
