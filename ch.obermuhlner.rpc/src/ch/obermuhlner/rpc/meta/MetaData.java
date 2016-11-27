package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ch.obermuhlner.rpc.RpcServiceException;

@XmlRootElement
public class MetaData {

	@XmlElement(name = "services")
	private final ServiceDefinitionList serviceDefinitions = new ServiceDefinitionList();
	
	@XmlElement(name = "structs")
	private final StructDefinitionList structDefinitions = new StructDefinitionList();
	
	public boolean addServiceDefinition(ServiceDefinition serviceDefinition) {
		ServiceDefinition existing = serviceDefinitions.findByTemplate(serviceDefinition);
		
		if (existing != null) {
			checkMatch(existing, serviceDefinition);
			return false;
		} else {
			serviceDefinitions.add(serviceDefinition);
			return true;
		}
	}

	public boolean addStructDefinition(StructDefinition structDefinition) {
		StructDefinition existing = structDefinitions.findByTemplate(structDefinition);
		
		if (existing != null) {
			checkMatch(existing, structDefinition);
			return false;
		} else {
			structDefinitions.add(structDefinition);
			return true;
		}
	}

	public ServiceDefinitionList getServiceDefinitions() {
		return serviceDefinitions;
	}

	public StructDefinitionList getStructDefinitions() {
		return structDefinitions;
	}

	private void checkMatch(ServiceDefinition existing, ServiceDefinition update) {
		checkEqual("service.name", existing.name, update.name);
		checkEqual("service.javaTypeName", existing.javaTypeName, update.javaTypeName);
	}

	private void checkMatch(StructDefinition existing, StructDefinition update) {
		checkEqual("struct.name", existing.name, update.name);
		checkEqual("struct.javaTypeName", existing.javaTypeName, update.javaTypeName);
	}

	private void checkEqual(String name, Object existing, Object update) {
		if (!existing.equals(update)) {
			throw new RpcServiceException("Conflicting meta data: '" + name + "' existing='" + existing + "', update='" + update + "'");
		}
	}
	
}
