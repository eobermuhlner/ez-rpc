package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class StructDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaClass;

	@XmlElement(name = "field")
	public List<FieldDefinition> fieldDefinitions = new ArrayList<>();
	
	@SuppressWarnings("unused")
	private StructDefinition() {
		// for Jaxb
	}
	
	public StructDefinition(String name, String javaClass) {
		this.name = name;
		this.javaClass = javaClass;
	}
	
	public FieldDefinition findFieldDefinition(String name) {
		return fieldDefinitions.stream()
			.filter(fieldDefinition -> fieldDefinition.name.equals(name))
			.findFirst()
			.orElse(null);
	}

	@Override
	public String toString() {
		return "StructDefinition [name=" + name + ", javaClass=" + javaClass + ", fieldDefinitions=" + fieldDefinitions + "]";
	}
}
