package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class StructDefinitionList {
	
	@XmlElement(name = "struct")
	private final List<StructDefinition> structDefinitions = new ArrayList<>();
	
	public void add(StructDefinition structDefinition) {
		structDefinitions.add(structDefinition);
	}
	
	public List<StructDefinition> get() {
		return structDefinitions;
	}
	
	public StructDefinition findByName(String name) {
		return structDefinitions.stream()
				.filter(structDefinition -> structDefinition.name.equals(name))
				.findFirst()
				.orElse(null);
	}
	
	public StructDefinition findByType(String javaTypeName) {
		return structDefinitions.stream()
				.filter(structDefinition -> structDefinition.javaTypeName.equals(javaTypeName))
				.findFirst()
				.orElse(null);
	}
	
	public StructDefinition findByTemplate(StructDefinition template) {
		return structDefinitions.stream()
				.filter(structDefinition -> structDefinition.name.equals(template.name))
				.filter(structDefinition -> structDefinition.javaTypeName.equals(template.javaTypeName))
				.findFirst()
				.orElse(null);
	}
}
