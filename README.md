# ez-rpc

Easy RPC between Java, C++.

The goal of this project is to provide straightforward RPC (remote procedure calls) between applications independent of the programming language or transport layer.

The client and server code can be developed completely unaware of the ez-rpc framework.

Supported languages:
* Java
* ... (currently under development)

Supported transport layers:
* TCP Sockets
* ... (currently under development)

Suppoerted protocols:
* Java serialization (only between Java applications)
* binary protocol
* XML (currently under development)

## Approach


## Service Interface used over RPC

Interfaces can optionally be annotated to provide additional information for the RPC framework.

```java
@RpcService(name = "HelloService")
public interface HelloService {

	void ping();
	
	double calculateSquare(double value);
	
	@RpcMethod(name = "enrichExample")
	ExampleData exampleMethod(
			@RpcParameter(name = "poor")
			ExampleData exampleData);
}
```

## Data structures used ove RPC

Data structures over RPC are limited to the most important data types.

```java
@RpcStruct(name = "ExampleData")
public class ExampleData {
	public boolean booleanField;
	public int intField;
	public long longField;
	public String stringField;
	public List<String> listField;
	public Set<String> setField;
	public Map<Object, Object> mapField;
	public ExampleData nestedExampleData;
}
```

## Configuration

The configuration API is designed to be easy to use in injection frameworks (for example Spring).

### Client Configuration

```java
		MetaDataService serviceMetaData = new MetaDataService();
		serviceMetaData.load(new File("rpc-metadata.xml"));
		serviceMetaData.registerService(HelloService.class);
		serviceMetaData.save(new File("rpc-metadata.xml"));
		
		int port = 5924;
		StructureProtocol<Object> protocol = ProtocolFactory.binaryProtocol(serviceMetaData, HelloServiceImpl.class.getClassLoader());
		SocketClientTransport socketClientTransport = new SocketClientTransport(protocol, "localhost", port);
		
		ServiceFactory serviceFactory = new ServiceFactory();
		HelloService proxyService = serviceFactory.createRemoteService(HelloService.class, HelloServiceAsync.class, socketClientTransport);
```

### Server Configuration

```java
		MetaDataService serviceMetaData = new MetaDataService();
		serviceMetaData.load(new File("rpc-metadata.xml"));
		serviceMetaData.registerService(HelloService.class);
		serviceMetaData.save(new File("rpc-metadata.xml"));

		int port = 5924;
		StructureProtocol<Object> protocol = ProtocolFactory.binaryProtocol(serviceMetaData, HelloServiceImpl.class.getClassLoader());
		
		SocketServerTransport socketServerTransport = new SocketServerTransport(protocol, port);

		ServiceFactory serviceFactory = new ServiceFactory();
		serviceFactory.publishService(HelloService.class, helloServiceImpl, socketServerTransport);
```

## Meta Data

The meta data describes the services and data structures.

The meta data file is optional and can be generated automatically from the Java interfaces (with annotations).

Once the meta data file is created it becomes the master and the application will validate itself against it on startup.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<metaData>
    <services>
        <service name="HelloService" javaTypeName="ch.obermuhlner.rpc.example.api.HelloService">
            <method name="calculateSquare">
                <parameter name="arg0"/>
            </method>
            <method name="enrichExample" returnType="STRUCT" returnStructName="ExampleData">
                <parameter name="poor" type="STRUCT" structName="ExampleData"/>
            </method>
            <method name="ping"/>
        </service>
    </services>
    <structs>
        <struct name="ExampleData" javaTypeName="ch.obermuhlner.rpc.example.api.ExampleData">
            <field name="booleanField" type="BOOL"/>
            <field name="intField" type="INT"/>
            <field name="longField" type="LONG"/>
            <field name="stringField" type="STRING"/>
            <field name="listField" type="LIST"/>
            <field name="setField" type="SET"/>
            <field name="mapField" type="MAP"/>
            <field name="nestedExampleData" type="STRUCT" structName="ExampleData"/>
        </struct>
    </structs>
</metaData>
```

