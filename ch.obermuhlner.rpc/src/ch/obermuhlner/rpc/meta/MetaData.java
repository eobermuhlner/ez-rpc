package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ch.obermuhlner.rpc.RpcServiceException;

@XmlRootElement
public class MetaData {

	@XmlElement(name = "struct")
	private final List<StructDefinition> structDefinitions = new ArrayList<>();
	
	public List<StructDefinition> getStructDefinitions() {
		return Collections.unmodifiableList(structDefinitions);
	}
	
	public void addStructDefinition(StructDefinition structDefinition) {
		Optional<StructDefinition> existing = structDefinitions.stream().filter(struct -> struct.name.equals(structDefinition.name)).findFirst();
		
		if (existing.isPresent()) {
			checkMatch(existing.get(), structDefinition);
		} else {
			structDefinitions.add(structDefinition);
		}
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
