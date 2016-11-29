package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlAttribute;

public class ParameterDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaName;
	
	@XmlAttribute
	public String type;

	@Override
	public String toString() {
		return "ParameterDefinition [name=" + name + ", javaName=" + javaName + ", type=" + type + "]";
	}
}
