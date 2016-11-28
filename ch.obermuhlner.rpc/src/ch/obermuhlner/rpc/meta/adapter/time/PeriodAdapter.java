package ch.obermuhlner.rpc.meta.adapter.time;

import java.time.Period;

import ch.obermuhlner.rpc.annotation.RpcStruct;
import ch.obermuhlner.rpc.meta.adapter.Adapter;

@RpcStruct(name = "Period")
public class PeriodAdapter implements Adapter<Period, PeriodStruct> {

	@Override
	public Class<Period> getLocalType() {
		return Period.class;
	}

	@Override
	public Class<PeriodStruct> getRemoteType() {
		return PeriodStruct.class;
	}

	@Override
	public PeriodStruct convertLocalToRemote(Period local) {
		PeriodStruct remote = new PeriodStruct();
		remote.years = local.getYears();
		remote.months = local.getMonths();
		remote.days = local.getDays();
		return remote;
	}

	@Override
	public Period convertRemoteToLocal(PeriodStruct remote) {
		Period local = Period.of(remote.years, remote.months, remote.days);
		return local;
	}

}
