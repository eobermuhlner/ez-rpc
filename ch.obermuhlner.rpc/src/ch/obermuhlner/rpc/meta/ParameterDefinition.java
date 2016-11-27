package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlAttribute;

public class ParameterDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public Type type;
	
	@XmlAttribute
	public String structName; // only set if type == Type.STRUCT
	
	public ParameterDefinition() {
		// for Jaxb
	}

	public ParameterDefinition(String name, Type type, String structName) {
		this.name = name;
		this.type = type;
		this.structName = structName;
	}

	@Override
	public String toString() {
		return "ParameterDefinition [name=" + name + ", type=" + type + ", structName=" + structName + "]";
	}
}
