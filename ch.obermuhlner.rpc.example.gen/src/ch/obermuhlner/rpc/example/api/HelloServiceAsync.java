// Generated with ch.obermuhlner.rpc.annotation.generator.java.JavaRpcGenerator

package ch.obermuhlner.rpc.example.api;

import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.example.api.AdapterExampleData;
import ch.obermuhlner.rpc.example.api.ExampleData;

public interface HelloServiceAsync {

   CompletableFuture<Double> calculateSquareAsync(
      Double arg0
      );

   CompletableFuture<ExampleData> exampleMethodAsync(
      ExampleData poor
      );

   CompletableFuture<AdapterExampleData> adapterExampleMethodAsync(
      AdapterExampleData data
      );

}
