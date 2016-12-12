package ch.obermuhlner.rpc.meta;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class EnumDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaName;

	@XmlElement
	public List<String> values;

	public String getJavaName() {
		return javaName == null ? name : javaName;
	}

	@Override
	public String toString() {
		return "EnumDefinition [name=" + name + ", javaName=" + javaName + ", values=" + values + "]";
	}
}
