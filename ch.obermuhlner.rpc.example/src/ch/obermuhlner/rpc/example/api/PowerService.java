package ch.obermuhlner.rpc.example.api;

import ch.obermuhlner.rpc.annotation.RpcService;

@RpcService(name = "PowerService")
public interface PowerService {

	double creditWorthy(Person person);
	
}
