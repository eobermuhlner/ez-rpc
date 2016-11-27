package ch.obermuhlner.rpc.meta.adapter;

import java.math.BigDecimal;

import ch.obermuhlner.rpc.annotation.RpcStruct;

public class BigDecimalAdapter implements Adapter<BigDecimal, BigDecimalAdapter.BigDecimalStruct> {

	@RpcStruct(name = "BigDecimal")
	public static class BigDecimalStruct {
		public String value;
	}

	@Override
	public Class<BigDecimal> getLocalType() {
		return BigDecimal.class;
	}

	@Override
	public Class<BigDecimalStruct> getRemoteType() {
		return BigDecimalStruct.class;
	}

	@Override
	public BigDecimalStruct convertLocalToRemote(BigDecimal localType) {
		BigDecimalStruct remoteType = new BigDecimalStruct();
		remoteType.value = localType.toString();
		return remoteType;
	}

	@Override
	public BigDecimal convertStructToType(BigDecimalStruct remoteType) {
		BigDecimal localType = new BigDecimal(remoteType.value);
		return localType;
	}
}
