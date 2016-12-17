package ch.obermuhlner.rpc.meta;

public enum Type {
	BOOL("boolean", "Boolean"),
	INT("int", "Integer"),
	LONG("long", "Long"),
	DOUBLE("double", "Double"),
	STRING("String", "String"),
	LIST("List", "List"),
	SET("Set", "Set"),
	MAP("Map", "Map"),
	STRUCT(null, null),
	ENUM(null, null);
	
	private String javaPrimitiveTypeName;

	private String javaClassTypeName;

	private Type(String javaPrimitiveTypeName, String javaClassTypeName) {
		this.javaPrimitiveTypeName = javaPrimitiveTypeName;
		this.javaClassTypeName = javaClassTypeName;
	}
	
	public String toJavaPrimitiveTypeName() {
		return javaPrimitiveTypeName;
	}
	
	public String toJavaClassTypeName() {
		return javaClassTypeName;
	}
	
	public String toTypeName() {
		return name().toLowerCase();
	}
}
