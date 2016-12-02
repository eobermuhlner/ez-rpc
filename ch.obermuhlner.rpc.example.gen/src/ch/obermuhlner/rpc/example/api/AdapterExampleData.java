// Generated with ch.obermuhlner.rpc.annotation.generator.java.JavaRpcGenerator

package ch.obermuhlner.rpc.example.api;

import ch.obermuhlner.rpc.annotation.RpcField;
import ch.obermuhlner.rpc.annotation.RpcStruct;

import ch.obermuhlner.rpc.meta.adapter.bigdecimal.BigDecimalStruct;
import ch.obermuhlner.rpc.meta.adapter.time.DateStruct;
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
   public DateStruct localDateField;

   @RpcField()
   public PeriodStruct periodField;

}
