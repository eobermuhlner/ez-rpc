package ch.obermuhlner.rpc.meta.adapter.time;

import java.time.LocalDate;

import ch.obermuhlner.rpc.meta.adapter.Adapter;

public class LocalDateAdapter implements Adapter<LocalDate, DateStruct> {

	@Override
	public Class<LocalDate> getLocalType() {
		return LocalDate.class;
	}

	@Override
	public Class<DateStruct> getRemoteType() {
		return DateStruct.class;
	}

	@Override
	public DateStruct convertLocalToRemote(LocalDate local) {
		DateStruct remote = new DateStruct();
		remote.year = local.getYear();
		remote.month = local.getMonthValue();
		remote.day = local.getDayOfMonth();
		return remote;
	}

	@Override
	public LocalDate convertRemoteToLocal(DateStruct remote) {
		return LocalDate.of(remote.year, remote.month, remote.day);
	}

}
