package ch.obermuhlner.rpc.annotation.generator.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import ch.obermuhlner.rpc.meta.FieldDefinition;
import ch.obermuhlner.rpc.meta.MetaData;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.meta.ServiceDefinition;
import ch.obermuhlner.rpc.meta.StructDefinition;

public class JavaRpcGenerator {

	private static final String INDENT = "   ";
	
	private MetaDataService metaDataService;

	public JavaRpcGenerator(MetaDataService metaDataService) {
		this.metaDataService = metaDataService;
	}

	public void generate() {
		MetaData metaData = metaDataService.getMetaData();
		
		for (StructDefinition structDefinition : metaData.getStructDefinitions().get()) {
			generate(structDefinition);
		}
		
		for (ServiceDefinition serviceDefinition : metaData.getServiceDefinitions().get()) {
			generate(serviceDefinition);
		}
	}

	private void generate(StructDefinition structDefinition) {
		System.out.println(structDefinition);
		try (PrintWriter out = new PrintWriter(toJavaFile(structDefinition.javaClass))) {
			String packageName = toPackageName(structDefinition.javaClass);
			String className = toClassName(structDefinition.javaClass);
			
			if (packageName != null) {
				out.print("package ");
				out.print(packageName);
				out.print(";");
				out.println();
				out.println();
			}

			out.print("@RpcStruct(name = \"");
			out.print(structDefinition.name);
			out.print("\")");
			out.println();
			
			out.print("public class ");
			out.print(className);
			out.print(" {");
			out.println();
			out.println();
			
			for (FieldDefinition fieldDefinition : structDefinition.fieldDefinitions) {
				{
					out.print(INDENT);
					out.print("@RpcField(");
					boolean needComma = false;
					if (fieldDefinition.element != null) {
						if (needComma) {
							out.print(", ");
						}
						out.print("element=");
						out.print(metaDataService.toJavaClassSignature(fieldDefinition.element));
						out.print(".class");
						needComma = true;
					}
					if (fieldDefinition.key != null) {
						if (needComma) {
							out.print(", ");
						}
						out.print("key=");
						out.print(metaDataService.toJavaClassSignature(fieldDefinition.key));
						out.print(".class");
						needComma = true;
					}
					if (fieldDefinition.value != null) {
						if (needComma) {
							out.print(", ");
						}
						out.print("value=");
						out.print(metaDataService.toJavaClassSignature(fieldDefinition.value));
						out.print(".class");
						needComma = true;
					}
					out.print(")");
					out.println();
				}
				
				{
					out.print(INDENT);
					out.print("public ");
					out.print(metaDataService.toJavaSignature(fieldDefinition));
					out.print(" ");
					out.print(fieldDefinition.name);
					out.print(";");
					out.println();
					out.println();
				}
			}

			out.print("}");
			out.println();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void generate(ServiceDefinition serviceDefinition) {
	}

	private File toJavaFile(String javaClass) {
		String packageName = toPackageName(javaClass);
		String className = toClassName(javaClass);
		
		Path packagePath = Paths.get(".", packageName.split(Pattern.quote(".")));
		packagePath.toFile().mkdirs();
		
		return packagePath.resolve(className + ".java").toFile();
	}

	private String toPackageName(String javaClass) {
		int index = javaClass.lastIndexOf('.');
		if (index < 0) {
			return null;
		}
		return javaClass.substring(0, index);
	}

	private String toClassName(String javaClass) {
		int index = javaClass.lastIndexOf('.');
		if (index < 0) {
			return javaClass;
		}
		return javaClass.substring(index + 1);
	}

	public static void main(String[] args) {
		MetaDataService metaDataService = new MetaDataService();
		for (String file : args) {
			metaDataService.load(new File(file));
		}
		
		JavaRpcGenerator javaRpcGenerator = new JavaRpcGenerator(metaDataService);
		javaRpcGenerator.generate();
	}

}
