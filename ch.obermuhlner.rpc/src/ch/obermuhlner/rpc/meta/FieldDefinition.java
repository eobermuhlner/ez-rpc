package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlAttribute;

public class FieldDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String type;
	
	public FieldDefinition() {
		// for Jaxb
	}

	public FieldDefinition(String name, String type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return "FieldDefinition [name=" + name + ", type=" + type + "]";
	}
}
