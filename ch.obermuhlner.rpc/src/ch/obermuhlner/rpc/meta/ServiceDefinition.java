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

	public MethodDefinition findByTemplate(MethodDefinition template) {
		return methodDefinitions.stream()
				.filter(methodDefinition -> methodDefinition.name.equals(template.name) || (methodDefinition.javaName != null && methodDefinition.javaName.equals(template.javaName)))
				.findFirst()
				.orElse(null);
	}

	@Override
	public String toString() {
		return "ServiceDefinition [name=" + name + ", javaName=" + javaName + ", sessionType=" + sessionType + ", methodDefinitions=" + methodDefinitions + "]";
	}
	
}
