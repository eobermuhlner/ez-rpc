package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ServiceDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaClass;
	
	@XmlAttribute
	public Type sessionType;
	
	@XmlAttribute
	public String sessionStruct;
	
	@XmlElement(name = "method")
	public List<MethodDefinition> methodDefinitions = new ArrayList<>();
	
	@SuppressWarnings("unused")
	private ServiceDefinition() {
		// for Jaxb
	}
	
	public ServiceDefinition(String name, String javaClass, Type sessionType, String sessionStruct) {
		this.name = name;
		this.javaClass = javaClass;
		this.sessionType = sessionType;
		this.sessionStruct = sessionStruct;
	}

	@Override
	public String toString() {
		return "StructDefinition [name=" + name + ", javaClass=" + javaClass + ", methodDefinitions=" + methodDefinitions + "]";
	}
}
