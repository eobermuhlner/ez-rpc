// Generated with ch.obermuhlner.rpc.annotation.generator.java.JavaRpcGenerator

package ch.obermuhlner.rpc.example.api;

import ch.obermuhlner.rpc.annotation.RpcField;
import ch.obermuhlner.rpc.annotation.RpcStruct;

import ch.obermuhlner.rpc.example.api.ExampleData;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RpcStruct(name = "ExampleData")
public class ExampleData {

   @RpcField()
   public boolean booleanField;

   @RpcField()
   public int intField;

   @RpcField()
   public long longField;

   @RpcField()
   public String stringField;

   @RpcField(element=String.class)
   public List<String> listField;

   @RpcField(element=String.class)
   public Set<String> setField;

   @RpcField(key=Integer.class, value=String.class)
   public Map<Integer, String> mapField;

   @RpcField()
   public ExampleData nestedExampleData;

}
