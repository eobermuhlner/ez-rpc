package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlAttribute;

public class ArgumentDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public Type type;
	
	@XmlAttribute
	public String structName; // only set if type == Type.STRUCT
	
	public ArgumentDefinition() {
		// for Jaxb
	}

	public ArgumentDefinition(String name, Type type, String structName) {
		this.name = name;
		this.type = type;
		this.structName = structName;
	}

	@Override
	public String toString() {
		return "ArgumentDefinition [name=" + name + ", type=" + type + ", structName=" + structName + "]";
	}
}
