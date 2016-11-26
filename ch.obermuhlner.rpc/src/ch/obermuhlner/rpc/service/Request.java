package ch.obermuhlner.rpc.service;

import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public final String serviceName;
	public final String methodName;
	public final Object[] arguments;
	
	public Request(String serviceName, String methodName, Object[] arguments) {
		this.serviceName = serviceName;
		this.methodName = methodName;
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		return "Request [serviceName=" + serviceName + ", methodName=" + methodName + ", arguments=" + Arrays.toString(arguments) + "]";
	}

}
