package com.lisb.msgpack.bind;

import com.lisb.msgpack.bind.unmarshaller.*;
import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

class Unmarshallers {
    static final String SUFFIX_UNMARSHALLER = "Unmarshaller";

    private static final List<Initializer> unmarshallers;

    static {
        unmarshallers = new ArrayList<>();

        unmarshallers.add(new SingletonInitializer(Integer.class, IntegerUnmarshaller.class));
        unmarshallers.add(new SingletonInitializer(Long.class, LongUnmarshaller.class));
        unmarshallers.add(new SingletonInitializer(Short.class, ShortUnmarshaller.class));
        unmarshallers.add(new SingletonInitializer(Float.class, FloatUnmarshaller.class));
        unmarshallers.add(new SingletonInitializer(Double.class, DoubleUnmarshaller.class));
        unmarshallers.add(new SingletonInitializer(BigInteger.class, BigIntegerUnmarshaller.class));
        unmarshallers.add(new SingletonInitializer(Boolean.class, BooleanUnmarshaller.class));
        unmarshallers.add(new SingletonInitializer(Byte.class, ByteUnmarshaller.class));
        unmarshallers.add(new SingletonInitializer(String.class, StringUnmarshaller.class));

        unmarshallers.add(new ArrayInitializer());

        unmarshallers.add(new EnumInitializer());

        unmarshallers.add(new ArrayListInitializer());

        unmarshallers.add(new HashSetInitializer());

        unmarshallers.add(new HashMapInitializer());
    }

    private Unmarshallers() {
    }

    static CodeBlock getUnmarshaller(Types types, Elements elements, TypeMirror type) {
        if (type.getKind().isPrimitive()) {
            type = types.boxedClass((PrimitiveType) type).asType();
        }

        for (final Initializer unmarshaller : unmarshallers) {
            if (unmarshaller.match(types, elements, type)) {
                return unmarshaller.initialize(types, elements, type);
            }
        }

        final Element element = types.asElement(type);
        final String unmarshallerName = Utils.getBaseClassName(element) + SUFFIX_UNMARSHALLER;
        final PackageElement packageElement = elements.getPackageOf(element);
        final TypeElement typeElement = elements.getTypeElement(packageElement.isUnnamed()
                ? unmarshallerName : packageElement.getQualifiedName() + "." + unmarshallerName);
        return CodeBlock.builder().add("new $T()", typeElement).build();
    }

    private interface Initializer {
        CodeBlock initialize(Types types, Elements elements, TypeMirror type);

        boolean match(Types types, Elements elements, TypeMirror mirror);
    }

    private static class SingletonInitializer implements Initializer {

        private final Class<?> targetClass;
        private final Class<? extends Unmarshaller> unmarshaller;

        private SingletonInitializer(Class<?> targetClass, Class<? extends Unmarshaller> unmarshaller) {
            this.targetClass = targetClass;
            this.unmarshaller = unmarshaller;
        }

        @Override
        public CodeBlock initialize(Types types, Elements elements, TypeMirror type) {
            return CodeBlock.builder().add("$T.getInstance()", unmarshaller).build();
        }

        @Override
        public boolean match(Types types, Elements elements, TypeMirror mirror) {
            return types.isAssignable(elements.getTypeElement(targetClass.getName()).asType(), mirror);
        }
    }

    private static class EnumInitializer implements Initializer {
        @Override
        public CodeBlock initialize(Types types, Elements elements, TypeMirror type) {
            return CodeBlock.builder().add("new $T($T.class)", EnumUnmarshaller.class, type).build();
        }

        @Override
        public boolean match(Types types, Elements elements, TypeMirror mirror) {
            return types.asElement(mirror).getKind() == ElementKind.ENUM;
        }
    }

    private static class ArrayInitializer implements Initializer {

        @Override
        public CodeBlock initialize(Types types, Elements elements, TypeMirror type) {
            final TypeMirror componentType = ((ArrayType) type).getComponentType();
            return CodeBlock.builder().add("new $T($T.class, $L)", ArrayUnmarshaller.class,
                    componentType, getUnmarshaller(types, elements, componentType)).build();
        }

        @Override
        public boolean match(Types types, Elements elements, TypeMirror mirror) {
            return mirror.getKind() == TypeKind.ARRAY;
        }
    }

    private static class ArrayListInitializer implements Initializer {
        @Override
        public CodeBlock initialize(Types types, Elements elements, TypeMirror type) {
            final List<? extends TypeMirror> typeArguments = ((DeclaredType) type).getTypeArguments();
            if (typeArguments.isEmpty()) {
                throw new IllegalArgumentException("Cannot decide element unmarshaller. type:" + type);
            }
            return CodeBlock.builder().add("new $T($L)", CollectionUnmarshaller.ArrayListUnmarshaller.class,
                    getUnmarshaller(types, elements, typeArguments.get(0))).build();
        }

        @Override
        public boolean match(Types types, Elements elements, TypeMirror mirror) {
            final DeclaredType simpleType = types.getDeclaredType((TypeElement) types.asElement(mirror));
            return types.isAssignable(types.getDeclaredType(elements.getTypeElement(ArrayList.class.getName())),
                    simpleType);
        }
    }

    private static class HashSetInitializer implements Initializer {
        @Override
        public CodeBlock initialize(Types types, Elements elements, TypeMirror type) {
            final List<? extends TypeMirror> typeArguments = ((DeclaredType) type).getTypeArguments();
            if (typeArguments.isEmpty()) {
                throw new IllegalArgumentException("Cannot decide element unmarshaller. type:" + type);
            }
            return CodeBlock.builder().add("new $T($L)", CollectionUnmarshaller.HashSetUnmarshaller.class,
                    getUnmarshaller(types, elements, typeArguments.get(0))).build();
        }

        @Override
        public boolean match(Types types, Elements elements, TypeMirror mirror) {
            final DeclaredType simpleType = types.getDeclaredType((TypeElement) types.asElement(mirror));
            return types.isAssignable(types.getDeclaredType(elements.getTypeElement(HashSet.class.getName())),
                    simpleType);
        }
    }

    private static class HashMapInitializer implements Initializer {

        @Override
        public CodeBlock initialize(Types types, Elements elements, TypeMirror type) {
            final List<? extends TypeMirror> typeArguments = ((DeclaredType) type).getTypeArguments();
            if (typeArguments.size() < 2) {
                throw new IllegalArgumentException("Cannot decide element unmarshaller. type:" + type);
            }
            final CodeBlock keyUnmarshaller = getUnmarshaller(types, elements, typeArguments.get(0));
            final CodeBlock valueUnmarshaller = getUnmarshaller(types, elements, typeArguments.get(1));
            return CodeBlock.builder().add("new $T($L, $L)", MapUnmarshaller.class,
                    keyUnmarshaller, valueUnmarshaller).build();
        }

        @Override
        public boolean match(Types types, Elements elements, TypeMirror mirror) {
            final DeclaredType simpleType = types.getDeclaredType((TypeElement) types.asElement(mirror));
            return types.isAssignable(types.getDeclaredType(elements.getTypeElement(HashMap.class.getName())),
                    simpleType);
        }
    }
}
