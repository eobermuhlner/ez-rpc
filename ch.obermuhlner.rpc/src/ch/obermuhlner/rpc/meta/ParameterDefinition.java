package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ParameterDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaName;
	
	@XmlAttribute
	public String type;

	public String getJavaName() {
		return javaName == null ? name : javaName;
	}

	@Override
	public String toString() {
		return "ParameterDefinition [name=" + name + ", javaName=" + javaName + ", type=" + type + "]";
	}
}
