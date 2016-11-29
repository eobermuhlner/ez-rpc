package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class MethodDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaName;
	
	@XmlElement
	public String returns;
	
	@XmlElement(name = "parameter")
	public List<ParameterDefinition> parameterDefinitions = new ArrayList<>();
	
	@Override
	public String toString() {
		return "MethodDefinition [name=" + name + ", returns=" + returns + ", parameterDefinitions=" + parameterDefinitions + "]";
	}
}
