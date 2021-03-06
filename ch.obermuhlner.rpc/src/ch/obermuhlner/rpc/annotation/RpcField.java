package ch.obermuhlner.rpc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface RpcField {

	String name() default "";

	Class<?> element() default Void.class;
	
	Class<?> key() default Void.class;
	
	Class<?> value() default Void.class;
	
}
