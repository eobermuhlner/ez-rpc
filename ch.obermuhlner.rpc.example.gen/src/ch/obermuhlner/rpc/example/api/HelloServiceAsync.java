// Generated with ch.obermuhlner.rpc.annotation.generator.java.JavaRpcGenerator

package ch.obermuhlner.rpc.example.api;

import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.example.api.AdapterExampleData;
import ch.obermuhlner.rpc.example.api.ExampleData;

public interface HelloServiceAsync {

   CompletableFuture<ExampleData> exampleMethodAsync(
      ExampleData poor
      );

   CompletableFuture<Double> calculateSquareAsync(
      Double arg0
      );

   CompletableFuture<AdapterExampleData> adapterExampleMethodAsync(
      AdapterExampleData data
      );

}
