package ch.obermuhlner.rpc.example.api;

import java.util.List;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "Person")
public class Person {
	public String name;
	public int age;
	public List<String> hobbies;
}
