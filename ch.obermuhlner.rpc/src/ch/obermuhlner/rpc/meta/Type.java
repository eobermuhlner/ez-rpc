package ch.obermuhlner.rpc.meta;

public enum Type {
	BOOL,
	INT,
	LONG,
	DOUBLE,
	STRING,
	LIST,
	SET,
	MAP,
	STRUCT;
	
	public String toTypeName() {
		return name().toLowerCase();
	}
}
