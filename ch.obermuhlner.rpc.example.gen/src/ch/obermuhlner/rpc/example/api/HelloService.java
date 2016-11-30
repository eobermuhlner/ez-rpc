// Generated with ch.obermuhlner.rpc.annotation.generator.java.JavaRpcGenerator

package ch.obermuhlner.rpc.example.api;

import ch.obermuhlner.rpc.annotation.RpcMethod;
import ch.obermuhlner.rpc.annotation.RpcParameter;
import ch.obermuhlner.rpc.annotation.RpcService;

import ch.obermuhlner.rpc.example.api.AdapterExampleData;
import ch.obermuhlner.rpc.example.api.ExampleData;

@RpcService(name = "HelloService")
public interface HelloService {

   @RpcMethod()
   Double calculateSquare(
      @RpcParameter(name="arg0")
      Double arg0
      );

   @RpcMethod()
   void ping();

   @RpcMethod(name="enrichExample")
   ExampleData exampleMethod(
      @RpcParameter(name="poor")
      ExampleData poor
      );

   @RpcMethod()
   void exampleFailure();

   @RpcMethod(name="adapterExample")
   AdapterExampleData adapterExampleMethod(
      @RpcParameter(name="data")
      AdapterExampleData data
      );

}
