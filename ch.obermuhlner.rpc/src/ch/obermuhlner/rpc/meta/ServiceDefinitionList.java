package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import ch.obermuhlner.rpc.RpcServiceException;

// TODO duplicate of StructDefinitionList !
public class ServiceDefinitionList {
	
	@XmlElement(name = "service")
	private final List<ServiceDefinition> serviceDefinitions = new ArrayList<>();
	
	public void add(ServiceDefinition serviceDefinition) {
		ServiceDefinition existing = findByTemplate(serviceDefinition);
		if (existing != null) {
			throw new RpcServiceException("Failed to add " + serviceDefinition + " because " + existing + " already exists");
		}
		
		serviceDefinitions.add(serviceDefinition);
	}
	
	public List<ServiceDefinition> get() {
		return serviceDefinitions;
	}
	
	public ServiceDefinition findByName(String name) {
		return serviceDefinitions.stream()
				.filter(serviceDefinition -> serviceDefinition.name.equals(name))
				.findFirst()
				.orElse(null);
	}
	
	public ServiceDefinition findByType(String javaTypeName) {
		return serviceDefinitions.stream()
				.filter(serviceDefinition -> serviceDefinition.javaName.equals(javaTypeName))
				.findFirst()
				.orElse(null);
	}
	
	public ServiceDefinition findByTemplate(ServiceDefinition template) {
		return serviceDefinitions.stream()
				.filter(serviceDefinition -> serviceDefinition.name.equals(template.name))
				.filter(serviceDefinition -> serviceDefinition.javaName.equals(template.javaName))
				.findFirst()
				.orElse(null);
	}
}
