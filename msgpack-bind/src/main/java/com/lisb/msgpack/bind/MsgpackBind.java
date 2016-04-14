package com.lisb.msgpack.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface MsgpackBind {
    GenerateType value() default GenerateType.BOTH;

    enum GenerateType {
        MARSHALLER, UNMARSHALLER, BOTH
    }
}
