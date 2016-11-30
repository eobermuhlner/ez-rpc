package ch.obermuhlner.rpc.meta.adapter.bigdecimal;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.obermuhlner.rpc.annotation.RpcField;
import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "BigDecimal")
public class BigDecimalStruct {

   @RpcField()
   public String value;

}
