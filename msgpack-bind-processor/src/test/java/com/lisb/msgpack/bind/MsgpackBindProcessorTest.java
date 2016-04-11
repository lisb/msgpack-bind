package com.lisb.msgpack.bind;

import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class MsgpackBindProcessorTest {

    @Test
    public void testSimpleSource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("SimpleSource.java"))
                .processedWith(new MsgpackBindProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forResource("SimpleSourceMarshaller.java"));
    }

    @Test
    public void testChainedSource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("ChainedSource.java"))
                .processedWith(new MsgpackBindProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forResource("ChainedSourceMarshaller.java"));
    }

    @Test
    public void testPackagedSource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("com/lisb/msgpack/bind/PackagedSource.java"))
                .processedWith(new MsgpackBindProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forResource("com/lisb/msgpack/bind/PackagedSourceMarshaller.java"));
    }

}
