package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlAttribute;

public class ParameterDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String type;
	
	public ParameterDefinition() {
		// for Jaxb
	}

	public ParameterDefinition(String name, String type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return "ParameterDefinition [name=" + name + ", type=" + type + "]";
	}
}
