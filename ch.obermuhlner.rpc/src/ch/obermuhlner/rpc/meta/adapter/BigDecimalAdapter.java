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
	public BigDecimalStruct convertLocalToRemote(BigDecimal local) {
		BigDecimalStruct remote = new BigDecimalStruct();
		remote.value = local.toString();
		return remote;
	}

	@Override
	public BigDecimal convertRemoteToLocal(BigDecimalStruct remote) {
		BigDecimal localType = new BigDecimal(remote.value);
		return localType;
	}
}
