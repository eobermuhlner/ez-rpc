package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class MethodDefinition {

	@XmlAttribute
	public String name;
	
	@XmlElement
	public String returns;
	
	@XmlElement(name = "parameter")
	public List<ParameterDefinition> parameterDefinitions = new ArrayList<>();
	
	public MethodDefinition() {
		// for Jaxb
	}

	public MethodDefinition(String name, String returnType) {
		this.name = name;
		this.returns = returnType;
	}

	@Override
	public String toString() {
		return "MethodDefinition [name=" + name + ", returns=" + returns + ", parameterDefinitions=" + parameterDefinitions + "]";
	}
}
