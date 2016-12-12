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
	
	@XmlElement(name = "enums")
	private final EnumDefinitionList enumDefinitions = new EnumDefinitionList();
	
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

	public EnumDefinitionList getEnumDefinitions() {
		return enumDefinitions;
	}

	private void checkMatch(ServiceDefinition existingServiceDefinition, ServiceDefinition updateServiceDefinition) {
		checkEqual("service.name", existingServiceDefinition.name, updateServiceDefinition.name);
		
		for (MethodDefinition existingMethodDefinition : existingServiceDefinition.methodDefinitions) {
			MethodDefinition updateMethodDefinition = updateServiceDefinition.findByTemplate(existingMethodDefinition);
			if (updateMethodDefinition != null) {
				checkMatch(existingMethodDefinition, updateMethodDefinition);
			} else {
				throw new RpcMetaDataException("Missing meta data for service method: " + existingServiceDefinition.name + "." + existingMethodDefinition.name);
			}
		}
	}

	private void checkMatch(StructDefinition existing, StructDefinition update) {
		checkEqual("struct.name", existing.name, update.name);
	}

	private void checkMatch(MethodDefinition existingMethodDefinition, MethodDefinition updateMethodDefinition) {
		checkEqual("method.name", existingMethodDefinition.name, updateMethodDefinition.name);
		checkEqual("method.return", existingMethodDefinition.returns, updateMethodDefinition.returns);

		if (existingMethodDefinition.parameterDefinitions.size() != updateMethodDefinition.parameterDefinitions.size()) {
			throw new RpcMetaDataException("Wrong number of parameters in service method " + updateMethodDefinition.name);
		}
		
		for (int i = 0; i < existingMethodDefinition.parameterDefinitions.size(); i++) {
			ParameterDefinition existingParameterDefinition = existingMethodDefinition.parameterDefinitions.get(i);
			ParameterDefinition updateParameterDefinition = updateMethodDefinition.parameterDefinitions.get(i);

			checkMatch(existingParameterDefinition, updateParameterDefinition);
		}
	}

	private void checkMatch(ParameterDefinition existingParameterDefinition, ParameterDefinition updateParameterDefinition) {
		checkEqual("parameter.name", existingParameterDefinition.name, updateParameterDefinition.name);
		checkEqual("parameter.type", existingParameterDefinition.type, updateParameterDefinition.type);
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
