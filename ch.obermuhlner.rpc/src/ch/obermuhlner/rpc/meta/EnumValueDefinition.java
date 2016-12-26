package ch.obermuhlner.rpc.meta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class EnumValueDefinition {

	@XmlAttribute
	public Long id;
	
	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String javaName;

	public long getId() {
		if (id == null) {
			return MetaDataService.generateId(name);
		}
		return id;
	}
	
	public String getJavaName() {
		return javaName == null ? name : javaName;
	}

	@Override
	public String toString() {
		return "EnumValueDefinition [id=" + id + ", name=" + name + ", javaName=" + javaName + "]";
	}
}
