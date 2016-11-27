package ch.obermuhlner.rpc.meta;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

public class MethodDefinition {

	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public Type returnType;
	
	@XmlAttribute
	public String returnStructName; // only set if returnType == Type.STRUCT
	
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
