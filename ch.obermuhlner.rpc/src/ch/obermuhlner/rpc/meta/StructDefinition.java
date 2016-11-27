package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class StructDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaTypeName;

	@XmlElement(name = "field")
	public List<FieldDefinition> fieldDefinitions = new ArrayList<>();
	
	@SuppressWarnings("unused")
	private StructDefinition() {
		// for Jaxb
	}
	
	public StructDefinition(String name, String javaTypeName) {
		this.name = name;
		this.javaTypeName = javaTypeName;
	}

	@Override
	public String toString() {
		return "StructDefinition [name=" + name + ", javaTypeName=" + javaTypeName + ", fieldDefinitions=" + fieldDefinitions + "]";
	}
}
