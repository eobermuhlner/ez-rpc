package ch.obermuhlner.rpc.service;

import java.io.Serializable;

import ch.obermuhlner.rpc.annotation.RpcObject;

@RpcObject(name = "RpcResponse")
public class Response implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public Object result;
	public Exception exception;
	
	@Override
	public String toString() {
		return "Response [result=" + result + ", exception=" + exception + "]";
	}
}
