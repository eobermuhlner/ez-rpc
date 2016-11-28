package ch.obermuhlner.rpc.example.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;

import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "AdapterExampleData")
public class AdapterExampleData {
	public BigDecimal bigDecimalField;
	public Date dateField;
	public LocalDateTime localDateTimeField;
	public LocalDate localDateField;
	public Period periodField;

	@Override
	public String toString() {
		return "AdapterExampleData [bigDecimalField=" + bigDecimalField + ", dateField=" + dateField
				+ ", localDateTimeField=" + localDateTimeField + ", localDateField=" + localDateField + ", periodField="
				+ periodField + "]";
	}
}
