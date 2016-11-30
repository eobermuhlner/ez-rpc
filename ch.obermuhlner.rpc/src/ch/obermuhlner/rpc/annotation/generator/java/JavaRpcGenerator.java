package ch.obermuhlner.rpc.annotation.generator.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import ch.obermuhlner.rpc.meta.FieldDefinition;
import ch.obermuhlner.rpc.meta.MetaData;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.meta.MethodDefinition;
import ch.obermuhlner.rpc.meta.ParameterDefinition;
import ch.obermuhlner.rpc.meta.ServiceDefinition;
import ch.obermuhlner.rpc.meta.StructDefinition;
import ch.obermuhlner.rpc.meta.Type;

public class JavaRpcGenerator {

	private static final String INDENT = "   ";
	
	private static final String ASYNC_SUFFIX = "Async";
	
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
			generate(serviceDefinition, false);
			generate(serviceDefinition, true);
		}
	}

	private void generate(StructDefinition structDefinition) {
		if (isFrameworkClass(structDefinition.javaName)) {
			return;
		}

		File javaFile = toJavaFile(structDefinition.javaName);
		System.out.println("Generating " + javaFile);
		
		try (PrintWriter out = new PrintWriter(javaFile)) {
			printGeneratedWithComment(out);

			String packageName = toJavaPackageName(structDefinition.javaName);
			String className = toJavaClassName(structDefinition.javaName);
			
			Set<String> javaImports = new HashSet<>();
			for (FieldDefinition fieldDefinition : structDefinition.fieldDefinitions) {
				switch(metaDataService.findTypeByName(fieldDefinition.type)) {
				case STRUCT:
					javaImports.add(metaDataService.toJavaSignature(fieldDefinition));
					break;
				case LIST:
					javaImports.add(List.class.getName());
					break;
				case SET:
					javaImports.add(Set.class.getName());
					break;
				case MAP:
					javaImports.add(Map.class.getName());
					break;
				default:
				}
			}
			
			if (packageName != null) {
				out.print("package ");
				out.print(packageName);
				out.print(";");
				out.println();
				out.println();
			}

			out.println("import ch.obermuhlner.rpc.annotation.RpcField;");
			out.println("import ch.obermuhlner.rpc.annotation.RpcStruct;");
			out.println();
			javaImports.stream()
				.sorted()
				.forEach(importJavaClass -> {
					out.print("import ");
					out.print(importJavaClass);
					out.print(";");
					out.println();
				});
			if (!javaImports.isEmpty()) {
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

				out.print(INDENT);
				out.print("public ");
				out.print(toJavaClassName(metaDataService.toJavaSignature(fieldDefinition)));
				out.print(" ");
				out.print(fieldDefinition.name);
				out.print(";");
				out.println();
				out.println();
			}

			out.print("}");
			out.println();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private boolean isFrameworkClass(String javaTypeName) {
		try {
			Class.forName(javaTypeName);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	private void generate(ServiceDefinition serviceDefinition, boolean async) {
		String javaClass = serviceDefinition.getJavaName() + (async ? ASYNC_SUFFIX : "");
		File javaFile = toJavaFile(javaClass);
		System.out.println("Generating " + javaFile);
		
		try (PrintWriter out = new PrintWriter(javaFile)) {
			printGeneratedWithComment(out);
			
			String packageName = toJavaPackageName(javaClass);
			String className = toJavaClassName(javaClass);
			
			Set<String> javaImports = new HashSet<>();
			for (MethodDefinition methodDefinition : serviceDefinition.methodDefinitions) {
				addJavaImport(javaImports, methodDefinition.returns);
				
				for (ParameterDefinition parameterDefinition : methodDefinition.parameterDefinitions) {
					addJavaImport(javaImports, parameterDefinition.type);
				}
			}

			if (packageName != null) {
				out.print("package ");
				out.print(packageName);
				out.print(";");
				out.println();
				out.println();
			}

			if (async) {
				out.println("import java.util.concurrent.CompletableFuture;");
				out.println();
			}
			if (!async) {
				out.println("import ch.obermuhlner.rpc.annotation.RpcMethod;");
				out.println("import ch.obermuhlner.rpc.annotation.RpcParameter;");
				out.println("import ch.obermuhlner.rpc.annotation.RpcService;");
				out.println();
			}
			javaImports.stream()
				.sorted()
				.forEach(importJavaClass -> {
					out.print("import ");
					out.print(importJavaClass);
					out.print(";");
					out.println();
				});
			if (!javaImports.isEmpty()) {
				out.println();
			}

			if (!async) {
				out.print("@RpcService(");
				if (!serviceDefinition.name.equals(javaClass)) {
					out.print("name = \"");
					out.print(serviceDefinition.name);
					out.print("\"");
				}
				out.print(")");
				out.println();
			}
			
			out.print("public interface ");
			out.print(className);
			out.print(" {");
			out.println();
			out.println();
			
			for (MethodDefinition methodDefinition : serviceDefinition.methodDefinitions) {
				if (async) {
					if (methodDefinition.returns == null) {
						continue;
					}
				}
				
				if (!async) {
					out.print(INDENT);
					out.print("@RpcMethod(");
					if (methodDefinition.javaName != null && !methodDefinition.name.equals(methodDefinition.javaName)) {
						out.print("name=\"");
						out.print(methodDefinition.name);
						out.print("\"");
					}
					out.print(")");
					out.println();
				}
				
				out.print(INDENT);
				if (async) {
					out.print("CompletableFuture<");
				}
				out.print(orDefault(toJavaClassName(metaDataService.toJavaClassSignature(methodDefinition.returns)), "void"));
				if (async) {
					out.print(">");
				}
				out.print(" ");
				out.print(methodDefinition.getJavaName());
				if (async) {
					out.print(ASYNC_SUFFIX);
				}
				out.print("(");
				
				if (!methodDefinition.parameterDefinitions.isEmpty()) {
					out.println();

					for (int i = 0; i < methodDefinition.parameterDefinitions.size(); i++) {
						ParameterDefinition parameterDefinition = methodDefinition.parameterDefinitions.get(i);

						if (!async) {
							out.print(INDENT);
							out.print(INDENT);
							out.print("@RpcParameter(name=\"");
							out.print(parameterDefinition.name);
							out.print("\")");
							out.println();
						}
						
						out.print(INDENT);
						out.print(INDENT);
						out.print(toJavaClassName(metaDataService.toJavaClassSignature(parameterDefinition.type)));
						out.print(" ");
						out.print(toJavaClassName(parameterDefinition.getJavaName()));
						
						if (i != methodDefinition.parameterDefinitions.size() - 1) {
							out.print(",");
						}
						out.println();
					}

					out.print(INDENT);
					out.print(INDENT);
				}
				
				out.print(");");
				out.println();
				out.println();
			}
			
			out.print("}");
			out.println();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void addJavaImport(Set<String> javaImports, String typeString) {
		String javaType = metaDataService.toJavaClassSignature(typeString);
		Type type = metaDataService.findTypeByName(typeString);
		
		if (type == null) {
			return;
		}
		
		switch(type) {
		case STRUCT:
			javaImports.add(javaType);
			break;
		case LIST:
			javaImports.add(List.class.getName());
			break;
		case SET:
			javaImports.add(Set.class.getName());
			break;
		case MAP:
			javaImports.add(Map.class.getName());
			break;
		default:
		}
	}

	private void printGeneratedWithComment(PrintWriter out) {
		out.print("// Generated with ");
		out.print(getClass().getName());
		out.println();
		out.println();
	}

	private <T> T orDefault(T value, T defaultValue) {
		if (value == null) {
			return defaultValue; 
		} else {
			return value;
		}
	}

	private File toJavaFile(String javaClass) {
		String packageName = toJavaPackageName(javaClass);
		String className = toJavaClassName(javaClass);
		
		Path packagePath = Paths.get(".", packageName.split(Pattern.quote(".")));
		packagePath.toFile().mkdirs();
		
		return packagePath.resolve(className + ".java").toFile();
	}

	private String toJavaPackageName(String javaClass) {
		int index = javaClass.lastIndexOf('.');
		if (index < 0) {
			return null;
		}
		return javaClass.substring(0, index);
	}

	private String toJavaClassName(String javaClass) {
		if (javaClass == null) {
			return null;
		}
		
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
