package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlAttribute;

public class FieldDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public Type type;
	
	@XmlAttribute
	public String structName; // only set if type == Type.STRUCT
	
	public FieldDefinition() {
		// for Jaxb
	}

	public FieldDefinition(String name, Type type, String structName) {
		this.name = name;
		this.type = type;
		this.structName = structName;
	}

	@Override
	public String toString() {
		return "FieldDefinition [name=" + name + ", type=" + type + ", structName=" + structName + "]";
	}
}
