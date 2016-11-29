package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class MethodDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaName;
	
	@XmlElement
	public String returns;
	
	@XmlElement(name = "parameter")
	public List<ParameterDefinition> parameterDefinitions = new ArrayList<>();
	
	public String getJavaName() {
		return javaName == null ? name : javaName;
	}

	@Override
	public String toString() {
		return "MethodDefinition [name=" + name + ", returns=" + returns + ", parameterDefinitions=" + parameterDefinitions + "]";
	}
}
