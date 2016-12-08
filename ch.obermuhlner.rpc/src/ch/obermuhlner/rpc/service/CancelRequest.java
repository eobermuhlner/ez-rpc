package ch.obermuhlner.rpc.service;

import java.io.Serializable;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "RpcCancelRequest")
public class CancelRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String serviceName;
	public String methodName;
	public String requestId;

	@Override
	public String toString() {
		return "CancelRequest [serviceName=" + serviceName + ", methodName=" + methodName + ", requestId=" + requestId
				+ "]";
	}
}
