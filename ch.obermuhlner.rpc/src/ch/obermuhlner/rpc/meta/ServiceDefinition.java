package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ServiceDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaName;
	
	@XmlAttribute
	public String sessionType;
	
	@XmlElement(name = "method")
	public List<MethodDefinition> methodDefinitions = new ArrayList<>();

	@Override
	public String toString() {
		return "ServiceDefinition [name=" + name + ", javaName=" + javaName + ", sessionType=" + sessionType + ", methodDefinitions=" + methodDefinitions + "]";
	}
	
}
