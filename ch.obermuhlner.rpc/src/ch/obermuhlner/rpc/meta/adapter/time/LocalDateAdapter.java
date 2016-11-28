package ch.obermuhlner.rpc.meta.adapter.time;

import java.time.LocalDate;

import ch.obermuhlner.rpc.meta.adapter.Adapter;

public class LocalDateAdapter implements Adapter<LocalDate, EpochDayStruct> {

	@Override
	public Class<LocalDate> getLocalType() {
		return LocalDate.class;
	}

	@Override
	public Class<EpochDayStruct> getRemoteType() {
		return EpochDayStruct.class;
	}

	@Override
	public EpochDayStruct convertLocalToRemote(LocalDate local) {
		EpochDayStruct remote = new EpochDayStruct();
		remote.days = local.toEpochDay();
		return remote;
	}

	@Override
	public LocalDate convertRemoteToLocal(EpochDayStruct remote) {
		return LocalDate.ofEpochDay(remote.days);
	}

}
