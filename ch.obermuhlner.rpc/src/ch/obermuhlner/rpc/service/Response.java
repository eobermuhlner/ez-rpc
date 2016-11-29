package ch.obermuhlner.rpc.service;

import java.io.Serializable;

import ch.obermuhlner.rpc.annotation.RpcStruct;
import ch.obermuhlner.rpc.data.DynamicStruct;

@RpcStruct(name = "RpcResponse")
public class Response implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public DynamicStruct result;
	public Object exception;
	
	@Override
	public String toString() {
		return "Response [result=" + result + ", exception=" + exception + "]";
	}
}
