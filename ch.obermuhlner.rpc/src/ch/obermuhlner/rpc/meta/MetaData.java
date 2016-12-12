package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ch.obermuhlner.rpc.exception.meta.RpcMetaDataException;

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

	private void checkMatch(ServiceDefinition existingServiceDefinition, ServiceDefinition updateServiceDefinition) {
		checkEqual("service.name", existingServiceDefinition.name, updateServiceDefinition.name);
		
		for (MethodDefinition existingMethodDefinition : existingServiceDefinition.methodDefinitions) {
			MethodDefinition updateMethodDefinition = updateServiceDefinition.findByTemplate(existingMethodDefinition);
			if (updateMethodDefinition != null) {
				checkMatch(existingMethodDefinition, updateMethodDefinition);
			} else {
				throw new RpcMetaDataException("Missing meta data for service method: " + existingMethodDefinition.name);
			}
		}
	}

	private void checkMatch(StructDefinition existing, StructDefinition update) {
		checkEqual("struct.name", existing.name, update.name);
	}

	private void checkMatch(MethodDefinition existing, MethodDefinition update) {
		checkEqual("method.name", existing.name, update.name);
	}

	private void checkEqual(String name, Object existing, Object update) {
		if (existing == null && update == null) {
			return;
		}
		if (!existing.equals(update)) {
			throw new RpcMetaDataException("Conflicting meta data: '" + name + "' existing='" + existing + "', update='" + update + "'");
		}
	}
	
}
