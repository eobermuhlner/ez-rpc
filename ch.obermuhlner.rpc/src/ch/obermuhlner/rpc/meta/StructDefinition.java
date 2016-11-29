package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class StructDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaName;

	@XmlElement(name = "field")
	public List<FieldDefinition> fieldDefinitions = new ArrayList<>();
	
	public String getJavaName() {
		return javaName == null ? name : javaName;
	}
	
	public FieldDefinition findFieldDefinition(String name) {
		return fieldDefinitions.stream()
			.filter(fieldDefinition -> fieldDefinition.name.equals(name))
			.findFirst()
			.orElse(null);
	}

	@Override
	public String toString() {
		return "StructDefinition [name=" + name + ", javaName=" + javaName + ", fieldDefinitions=" + fieldDefinitions + "]";
	}

}
