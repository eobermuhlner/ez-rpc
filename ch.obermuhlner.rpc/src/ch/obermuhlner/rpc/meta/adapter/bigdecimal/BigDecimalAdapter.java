package ch.obermuhlner.rpc.meta.adapter.bigdecimal;

import java.math.BigDecimal;

import ch.obermuhlner.rpc.meta.adapter.Adapter;

public class BigDecimalAdapter implements Adapter<BigDecimal, BigDecimalStruct> {

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
