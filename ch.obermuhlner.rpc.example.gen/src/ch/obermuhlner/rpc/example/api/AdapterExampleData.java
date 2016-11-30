package ch.obermuhlner.rpc.example.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.obermuhlner.rpc.annotation.RpcField;
import ch.obermuhlner.rpc.annotation.RpcStruct;

import ch.obermuhlner.rpc.meta.adapter.bigdecimal.BigDecimalStruct;
import ch.obermuhlner.rpc.meta.adapter.time.EpochDayStruct;
import ch.obermuhlner.rpc.meta.adapter.time.EpochMillisecondStruct;
import ch.obermuhlner.rpc.meta.adapter.time.PeriodStruct;

@RpcStruct(name = "AdapterExampleData")
public class AdapterExampleData {

   @RpcField()
   public BigDecimalStruct bigDecimalField;

   @RpcField()
   public EpochMillisecondStruct dateField;

   @RpcField()
   public EpochMillisecondStruct localDateTimeField;

   @RpcField()
   public EpochDayStruct localDateField;

   @RpcField()
   public PeriodStruct periodField;

}
