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
                .and()
                .generatesSources(JavaFileObjects.forResource("SimpleSourceMarshaller.java"),
                        JavaFileObjects.forResource("SimpleSourceUnmarshaller.java"));
    }

    @Test
    public void testChainedSource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("ChainedSource.java"))
                .processedWith(new MsgpackBindProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("ChainedSourceMarshaller.java"),
                        JavaFileObjects.forResource("ChainedSourceUnmarshaller.java"));
    }

    @Test
    public void testPackagedSource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("com/lisb/msgpack/bind/PackagedSource.java"))
                .processedWith(new MsgpackBindProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("com/lisb/msgpack/bind/PackagedSourceMarshaller.java"),
                        JavaFileObjects.forResource("com/lisb/msgpack/bind/PackagedSourceUnmarshaller.java"));
    }

    @Test
    public void testMarshallerOnlySource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("MarshallerOnlySource.java"))
                .processedWith(new MsgpackBindProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("MarshallerOnlySourceMarshaller.java"));
    }

    @Test
    public void testUnmarshallerOnlySource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("UnmarshallerOnlySource.java"))
                .processedWith(new MsgpackBindProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("UnmarshallerOnlySourceUnmarshaller.java"));
    }

    @Test
    public void testSubclassSource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("SubclassSource.java"))
                .processedWith(new MsgpackBindProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("SubclassSourceMarshaller.java"),
                        JavaFileObjects.forResource("SubclassSourceUnmarshaller.java"));
    }

    @Test
    public void testAccessTypePropertySource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("AccessTypePropertySource.java"))
                .processedWith(new MsgpackBindProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("AccessTypePropertySourceMarshaller.java"),
                        JavaFileObjects.forResource("AccessTypePropertySourceUnmarshaller.java"));
    }

    @Test
    public void testNoPairedPropertySource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("NoPairedPropertySource.java"))
                .processedWith(new MsgpackBindProcessor())
                .failsToCompile();
    }

    @Test
    public void testClassDifferentBetweenSetterAndGetterSource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("ClassDifferentBetweenSetterAndGetterSource.java"))
                .processedWith(new MsgpackBindProcessor())
                .failsToCompile();
    }

    @Test
    public void testSubclassPropertySource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("SubclassPropertySource.java"))
                .processedWith(new MsgpackBindProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("SubclassPropertySourceMarshaller.java"),
                        JavaFileObjects.forResource("SubclassPropertySourceUnmarshaller.java"));
    }

    @Test
    public void testDuplicatedFieldSource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("DuplicatedFieldSource.java"))
                .processedWith(new MsgpackBindProcessor())
                .failsToCompile();
    }

    @Test
    public void testGenericsSource() throws Throwable {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("SubGenericsSource.java"))
                .processedWith(new MsgpackBindProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("SubGenericsSourceMarshaller.java"),
                        JavaFileObjects.forResource("SubGenericsSourceUnmarshaller.java"));
    }
}
