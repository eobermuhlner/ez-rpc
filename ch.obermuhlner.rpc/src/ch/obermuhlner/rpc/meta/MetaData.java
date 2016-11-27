package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ch.obermuhlner.rpc.RpcServiceException;

@XmlRootElement
public class MetaData {

	@XmlElement(name = "structs")
	private final StructDefinitionList structDefinitions = new StructDefinitionList();
	
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

	public StructDefinitionList getStructDefinitions() {
		return structDefinitions;
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
