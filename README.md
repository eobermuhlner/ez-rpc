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

## Meta Data about services and data structures

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

