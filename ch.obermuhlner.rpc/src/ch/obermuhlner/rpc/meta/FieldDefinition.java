package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlAttribute;

public class FieldDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String type;

	@XmlAttribute
	public String element;

	@XmlAttribute
	public String key;

	@XmlAttribute
	public String value;
	
	public FieldDefinition() {
		// for Jaxb
	}

	public FieldDefinition(String name, String type, String element, String key, String value) {
		this.name = name;
		this.type = type;
		this.element = element;
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return "FieldDefinition [name=" + name + ", type=" + type + ", element=" + element + ", key=" + key + ", value=" + value + "]";
	}
}
