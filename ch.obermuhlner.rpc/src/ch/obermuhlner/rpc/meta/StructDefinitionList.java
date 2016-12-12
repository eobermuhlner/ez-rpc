package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import ch.obermuhlner.rpc.RpcException;

public class StructDefinitionList {
	
	@XmlElement(name = "struct")
	private final List<StructDefinition> structDefinitions = new ArrayList<>();
	
	public void add(StructDefinition structDefinition) {
		StructDefinition existing = findByTemplate(structDefinition);
		if (existing != null) {
			throw new RpcException("Failed to add " + structDefinition + " because " + existing + " already exists");
		}
		
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
				.filter(structDefinition -> structDefinition.getJavaName().equals(javaTypeName))
				.findFirst()
				.orElse(null);
	}
	
	public StructDefinition findByTemplate(StructDefinition template) {
		return structDefinitions.stream()
				.filter(structDefinition -> structDefinition.name.equals(template.name) || (structDefinition.javaName != null && structDefinition.getJavaName().equals(template.javaName)))
				.findFirst()
				.orElse(null);
	}
}
