package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import ch.obermuhlner.rpc.RpcException;

public class EnumDefinitionList {
	
	@XmlElement(name = "enum")
	private final List<EnumDefinition> enumDefinitions = new ArrayList<>();
	
	public void add(EnumDefinition enumDefinition) {
		EnumDefinition existing = findByTemplate(enumDefinition);
		if (existing != null) {
			throw new RpcException("Failed to add " + enumDefinition + " because " + existing + " already exists");
		}
		
		enumDefinitions.add(enumDefinition);
	}
	
	public List<EnumDefinition> get() {
		return enumDefinitions;
	}
	
	public EnumDefinition findByName(String name) {
		return enumDefinitions.stream()
				.filter(enumDefinition -> enumDefinition.name.equals(name))
				.findFirst()
				.orElse(null);
	}
	
	public EnumDefinition findByType(String javaTypeName) {
		return enumDefinitions.stream()
				.filter(enumDefinition -> enumDefinition.getJavaName().equals(javaTypeName))
				.findFirst()
				.orElse(null);
	}
	
	public EnumDefinition findByTemplate(EnumDefinition template) {
		return enumDefinitions.stream()
				.filter(enumDefinition -> enumDefinition.name.equals(template.name))
				.filter(enumDefinition -> enumDefinition.getJavaName().equals(template.javaName))
				.findFirst()
				.orElse(null);
	}
}
