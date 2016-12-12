package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ch.obermuhlner.rpc.RpcException;

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

	private void checkMatch(ServiceDefinition existing, ServiceDefinition update) {
		checkEqual("service.name", existing.name, update.name);
		checkEqual("service.javaName", existing.javaName, update.javaName);
	}

	private void checkMatch(StructDefinition existing, StructDefinition update) {
		checkEqual("struct.name", existing.name, update.name);
		checkEqual("struct.javaName", existing.javaName, update.javaName);
	}

	private void checkEqual(String name, Object existing, Object update) {
		if (!existing.equals(update)) {
			throw new RpcException("Conflicting meta data: '" + name + "' existing='" + existing + "', update='" + update + "'");
		}
	}
	
}
