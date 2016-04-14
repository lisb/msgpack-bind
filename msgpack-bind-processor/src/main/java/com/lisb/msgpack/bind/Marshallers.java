package com.lisb.msgpack.bind;

import com.lisb.msgpack.bind.marshaller.*;
import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.math.BigInteger;
import java.util.*;

class Marshallers {

    static final String SUFFIX_MARSHALLER = "Marshaller";

    private static final List<Initializer> marshallers;

    static {
        marshallers = new ArrayList<>();
        marshallers.add(new SingletonInitializer(Integer.class, IntegerMarshaller.class));
        marshallers.add(new SingletonInitializer(Long.class, LongMarshaller.class));
        marshallers.add(new SingletonInitializer(Short.class, ShortMarshaller.class));
        marshallers.add(new SingletonInitializer(Float.class, FloatMarshaller.class));
        marshallers.add(new SingletonInitializer(Double.class, DoubleMarshaller.class));
        marshallers.add(new SingletonInitializer(BigInteger.class, BigIntegerMarshaller.class));
        marshallers.add(new SingletonInitializer(Boolean.class, BooleanMarshaller.class));
        marshallers.add(new SingletonInitializer(Byte.class, ByteMarshaller.class));
        marshallers.add(new SingletonInitializer(String.class, StringMarshaller.class));

        marshallers.add(new ArrayInitializer());

        marshallers.add(new EnumInitializer());

        marshallers.add(new CollectionInitializer());

        marshallers.add(new MapInitializer());

    }

    private Marshallers() {
    }

    static CodeBlock getMarshaller(Types types, Elements elements, TypeMirror type) {
        if (type.getKind().isPrimitive()) {
            type = types.boxedClass((PrimitiveType) type).asType();
        }

        for (final Initializer marshaller : marshallers) {
            if (marshaller.match(types, elements, type)) {
                return marshaller.initialize(types, elements, type);
            }
        }

        final Element element = types.asElement(type);
        final String marshallerName = Utils.getBaseClassName(element) + SUFFIX_MARSHALLER;
        final PackageElement packageElement = elements.getPackageOf(element);
        final TypeElement typeElement = elements.getTypeElement(packageElement.isUnnamed()
                ? marshallerName : packageElement.getQualifiedName() + "." + marshallerName);
        return CodeBlock.builder().add("new $T()", typeElement).build();
    }

    private interface Initializer {
        CodeBlock initialize(Types types, Elements elements, TypeMirror type);

        boolean match(Types types, Elements elements, TypeMirror mirror);
    }

    private static class SingletonInitializer implements Initializer {
        private final Class<?> targetClass;
        private final Class<? extends Marshaller> marshaller;

        private SingletonInitializer(Class<?> targetClass, Class<? extends Marshaller> marshaller) {
            this.targetClass = targetClass;
            this.marshaller = marshaller;
        }

        @Override
        public CodeBlock initialize(Types types, Elements elements, TypeMirror type) {
            return CodeBlock.builder().add("$T.getInstance()", marshaller).build();
        }

        @Override
        public boolean match(Types types, Elements elements, TypeMirror mirror) {
            return Utils.isSameType(types, elements, mirror, targetClass);
        }
    }

    private static class CollectionInitializer implements Initializer {
        @Override
        public CodeBlock initialize(Types types, Elements elements, TypeMirror type) {
            final List<? extends TypeMirror> typeArguments = ((DeclaredType) type).getTypeArguments();
            if (typeArguments.isEmpty()) {
                throw new IllegalArgumentException("Cannot decide element marshaller. type:" + type);
            }
            return CodeBlock.builder().add("new $T($L)", CollectionMarshaller.class,
                    getMarshaller(types, elements, typeArguments.get(0))).build();
        }

        @Override
        public boolean match(Types types, Elements elements, TypeMirror mirror) {
            final DeclaredType simpleType = types.getDeclaredType((TypeElement) types.asElement(mirror));
            return Utils.isSameType(types, elements, simpleType, Collection.class) ||
                    Utils.isSameType(types, elements, simpleType, List.class) ||
                    Utils.isSameType(types, elements, simpleType, LinkedList.class) ||
                    Utils.isSameType(types, elements, simpleType, ArrayList.class) ||
                    Utils.isSameType(types, elements, simpleType, Set.class) ||
                    Utils.isSameType(types, elements, simpleType, HashSet.class);
        }

    }

    private static class EnumInitializer implements Initializer {
        @Override
        public CodeBlock initialize(Types types, Elements elements, TypeMirror type) {
            return CodeBlock.builder().add("new $T()", EnumMarshaller.class).build();
        }

        @Override
        public boolean match(Types types, Elements elements, TypeMirror mirror) {
            return types.asElement(mirror).getKind() == ElementKind.ENUM;
        }
    }

    private static class MapInitializer implements Initializer {
        @Override
        public CodeBlock initialize(Types types, Elements elements, TypeMirror type) {
            final List<? extends TypeMirror> typeArguments = ((DeclaredType) type).getTypeArguments();
            if (typeArguments.size() < 2) {
                throw new IllegalArgumentException("Cannot decide element marshaller. type:" + type);
            }
            final CodeBlock keyMarshaller = getMarshaller(types, elements, typeArguments.get(0));
            final CodeBlock valueMarshaller = getMarshaller(types, elements, typeArguments.get(1));
            return CodeBlock.builder().add("new $T($L, $L)", MapMarshaller.class,
                    keyMarshaller, valueMarshaller).build();
        }

        @Override
        public boolean match(Types types, Elements elements, TypeMirror mirror) {
            final DeclaredType simpleType = types.getDeclaredType((TypeElement) types.asElement(mirror));
            return Utils.isSameType(types, elements, simpleType, Map.class) ||
                    Utils.isSameType(types, elements, simpleType, HashMap.class);
        }
    }

    private static class ArrayInitializer implements Initializer {

        @Override
        public CodeBlock initialize(Types types, Elements elements, TypeMirror type) {
            return CodeBlock.builder().add("new $T($L)", ArrayMarshaller.class,
                    getMarshaller(types, elements, ((ArrayType) type).getComponentType())).build();
        }

        @Override
        public boolean match(Types types, Elements elements, TypeMirror mirror) {
            return mirror.getKind() == TypeKind.ARRAY;
        }
    }
}

