package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaName;
	
	@XmlAttribute
	public String sessionType;
	
	@XmlElement(name = "method")
	public List<MethodDefinition> methodDefinitions = new ArrayList<>();

	public String getJavaName() {
		return javaName == null ? name : javaName;
	}

	@Override
	public String toString() {
		return "ServiceDefinition [name=" + name + ", javaName=" + javaName + ", sessionType=" + sessionType + ", methodDefinitions=" + methodDefinitions + "]";
	}
	
}
