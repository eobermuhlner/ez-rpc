# ez-rpc

Easy RPC between Java and other languages.

The goal of this project is to provide straightforward RPC (remote procedure calls) between applications independent of the programming language or transport layer.

The client and server code can be developed completely unaware of the ez-rpc framework, you only have to be aware of some limitations in the supported data types (a common subset for all languages).

Supported languages:
* Java
* ... (currently under development)

Supported transport layers:
* TCP Sockets
* ... (currently under development)

Supported protocols:
* Java serialization (only between Java applications)
* Binary protocol
* XML (currently under development)
* ...

## Approach

The framework is designed so that the services can be written directly in your favorite programming language (for example Java)
and will work immediately as soon as they are implemented.

The specification of the services is then generated from the running services and can now be published between the different languages.

## Service used over RPC

Services consist of method specification that can be executed remotely.

### Java Service Interface

Service interfaces can optionally be annotated to provide additional information for the RPC framework.

The name in the annotation specifies the the standard name which will be used by other programming languages.
For each programming language a specific overriding name may be chosen (for example in Java a fully qualified name with package specification).

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

Additionally it is possible to provide a companion interface that specifies the asynchronous methods.
These methods are only available on the client side.
The implementation of the asynchronous methods will automatically be provided by the ez-rpc framework.

```java
public interface HelloServiceAsync {
	CompletableFuture<Double> calculateSquareAsync(double value);
}
```

## Data structures used over RPC

Data structures over RPC are limited to the most important data types.
* boolean
* int
* long
* double
* string
* list
* set
* map
* custom data structures

### Java Data structure

```java
@RpcStruct(name = "ExampleData")
public class ExampleData {
	public boolean booleanField;
	public int intField;
	public long longField;
	public String stringField;

	@RpcField(element=String.class)
	public List<String> listField;

	@RpcField(element=String.class)
	public Set<String> setField;

	@RpcField(key=Integer.class, value=String.class)
	public Map<Integer, String> mapField;

	public ExampleData nestedExampleData;
}
```

Java data structures may have methods, implement interfaces or use inheritance but none of these informations is transmitted over RPC.

## Configuration

The configuration API is designed to be easy to use in injection frameworks (for example Spring).

### Meta Data Configuration

The meta data is configured independently of the protocol or transport layer.
It specifies the services and data structures used by the RPC.

```java
	public static MetaDataService createMetaDataService() {
		MetaDataService metaDataService = new MetaDataService();
		metaDataService.load(new File("rpc-metadata.xml"));

		metaDataService.addAdapter(new BigDecimalAdapter());
		metaDataService.addAdapter(new DateAdapter());
		metaDataService.addAdapter(new LocalDateTimeAdapter());
		metaDataService.addAdapter(new LocalDateAdapter());
		metaDataService.addAdapter(new PeriodAdapter());

		metaDataService.registerService(HelloService.class);
		
		metaDataService.save(new File("rpc-metadata.xml"));
		
		return metaDataService;
	}
```

### TCP Client Configuration

A typical client configuration needs to specify:
* *MetaDataService* knows about the services and data structures
* *Protocol* specifies how the data is serialized and deserialized
* *Transport* specifies where the server is and how to communicate with it

The ez-rpc framework will then provide proxy implementations for the services that will send the calls to the remote server.

```java
		int port = 5924;
		String hostname = "localhost";
		
		MetaDataService metaDataService = HelloMetaData.createMetaDataService();
		StructureProtocol<Object> protocol = ProtocolFactory.binaryProtocol(metaDataService, HelloServiceImpl.class.getClassLoader());
		SocketClientTransport socketClientTransport = new SocketClientTransport(protocol, hostname, port);
		ServiceFactory serviceFactory = new ServiceFactory(metaDataService);
		
		HelloService proxyService = serviceFactory.createRemoteService(HelloService.class, HelloServiceAsync.class, socketClientTransport);
```

### TCP Server Configuration

A typical server configuration needs to specify:
* *MetaDataService* knows about the services and data structures
* *Protocol* specifies how the data is serialized and deserialized
* *Transport* specifies where the remote client is and how to communicate with it

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

The ez-rpc framework will then provide proxy implementations for the services that will receive the remote calls and delegate them to your implementation.

## Meta Data

The meta data describes the services and data structures used by the RPC framework.

The meta data file is optional and can be generated automatically from the Java interfaces (with annotations).

Once the meta data file is created it becomes the master specification and the application will validate itself against it on startup.

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

