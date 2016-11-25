package ch.obermuhlner.rpc.example.api;

import java.util.concurrent.CompletableFuture;

public interface HelloServiceAsync {

	CompletableFuture<Double> calculateSquareAsync(double value);

}
