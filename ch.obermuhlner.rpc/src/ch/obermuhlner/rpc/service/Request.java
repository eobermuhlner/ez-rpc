package ch.obermuhlner.rpc.service;

import java.io.Serializable;
import java.util.Arrays;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "RpcRequest")
public class Request implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String serviceName;
	public String methodName;
	public Object[] arguments;
	public Object session;

	@Override
	public String toString() {
		return "Request [serviceName=" + serviceName + ", methodName=" + methodName + ", arguments=" + Arrays.toString(arguments) + ", session=" + session + "]";
	}
}
