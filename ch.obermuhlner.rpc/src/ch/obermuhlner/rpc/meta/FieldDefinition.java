package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlAttribute;

public class FieldDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaName;
	
	@XmlAttribute
	public String type;

	@XmlAttribute
	public String element;

	@XmlAttribute
	public String key;

	@XmlAttribute
	public String value;

	@Override
	public String toString() {
		return "FieldDefinition [name=" + name + ", javaName=" + javaName + ", type=" + type + ", element=" + element + ", key=" + key + ", value=" + value + "]";
	}
}
