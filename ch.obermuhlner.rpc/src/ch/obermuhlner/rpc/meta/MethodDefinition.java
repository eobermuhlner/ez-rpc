package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class MethodDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public Type returnType;
	
	@XmlAttribute
	public String returnStructName; // only set if returnType == Type.STRUCT

	@XmlElement(name = "argument")
	public List<ArgumentDefinition> argumentDefinitions = new ArrayList<>();
	
	public MethodDefinition() {
		// for Jaxb
	}

	public MethodDefinition(String name, Type returnType, String returnStructName) {
		this.name = name;
		this.returnType = returnType;
		this.returnStructName = returnStructName;
	}
}
