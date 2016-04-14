package com.lisb.msgpack.bind;

import com.lisb.msgpack.bind.marshaller.ObjectMarshaller;
import com.lisb.msgpack.bind.unmarshaller.ObjectUnmarshaller;
import com.squareup.javapoet.*;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("com.lisb.msgpack.bind.MsgpackBind")
public class MsgpackBindProcessor extends AbstractProcessor {

    private static final boolean DEBUG = false;
    private static final String GETTER_PREFIX = "get";
    private static final String SETTER_PREFIX = "set";

    private Types types;
    private Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elements = processingEnv.getElementUtils();
        this.types = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (final Element element : roundEnv.getElementsAnnotatedWith(MsgpackBind.class)) {
            final TypeElement typeElement = (TypeElement) element;

            final MsgpackBind.GenerateType generateType = typeElement.getAnnotation(MsgpackBind.class).value();

            final List<Property> properties;
            if (Access.AccessType.FIELD.equals(getAccessType(typeElement))) {
                properties = getFields(typeElement);
            } else {
                properties = getProperties(typeElement);
            }

            final String baseClassName = Utils.getBaseClassName(typeElement);
            final String packageName = elements.getPackageOf(typeElement).getQualifiedName().toString();

            if (isGenerateMarshaller(generateType)) {
                final DeclaredType marshallerSupperClass = types.getDeclaredType(
                        elements.getTypeElement(ObjectMarshaller.class.getName()), typeElement.asType());
                final TypeSpec marshaller = TypeSpec.classBuilder(baseClassName + Marshallers.SUFFIX_MARSHALLER)
                        .addModifiers(Modifier.PUBLIC).superclass(TypeName.get(marshallerSupperClass))
                        .addFields(createMarshallerFields(typeElement, properties))
                        .addMethod(createMarshallerGetSizeMethod(typeElement, properties))
                        .addMethod(createMarshallerMarshalContentMethod(typeElement, properties))
                        .build();

                final JavaFile marshallerFile = JavaFile.builder(packageName, marshaller).build();
                try {
                    marshallerFile.writeTo(processingEnv.getFiler());

                    if (DEBUG) {
                        marshallerFile.writeTo(System.out);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (isGenerateUnmarshaller(generateType)) {
                final DeclaredType unmarshallerSuperClass = types.getDeclaredType(
                        elements.getTypeElement(ObjectUnmarshaller.class.getName()), typeElement.asType());
                final TypeSpec unmarshaller = TypeSpec.classBuilder(baseClassName + Unmarshallers.SUFFIX_UNMARSHALLER)
                        .addModifiers(Modifier.PUBLIC).superclass(TypeName.get(unmarshallerSuperClass))
                        .addFields(createUnmarshallerFields(typeElement, properties))
                        .addMethod(createUnmarshallerUnmarshalPropertyMethod(typeElement, properties))
                        .addMethod(createNewInstanceMethod(typeElement))
                        .build();

                final JavaFile unmarshallerFile = JavaFile.builder(packageName, unmarshaller).build();
                try {
                    unmarshallerFile.writeTo(processingEnv.getFiler());

                    if (DEBUG) {
                        unmarshallerFile.writeTo(System.out);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

    private Access.AccessType getAccessType(TypeElement element) {
        final Access access = element.getAnnotation(Access.class);
        return access != null ? access.value() : Access.AccessType.FIELD;
    }

    private List<FieldSpec> createMarshallerFields(TypeElement element, List<Property> properties) {
        final List<FieldSpec> fieldSpecs = new ArrayList<>(properties.size());
        for (final Property property : properties) {
            TypeMirror propertyType = property.getType();

            if (propertyType.getKind().isPrimitive()) {
                propertyType = types.boxedClass((PrimitiveType) propertyType).asType();
            }

            final DeclaredType newFieldType = types.getDeclaredType(elements
                    .getTypeElement(Marshaller.class.getName()), propertyType);
            final FieldSpec fieldSpec = FieldSpec.builder(TypeName.get(newFieldType),
                    property.getName() + Marshallers.SUFFIX_MARSHALLER, Modifier.PRIVATE, Modifier.FINAL)
                    .initializer(Marshallers.getMarshaller(types, elements, propertyType))
                    .build();
            fieldSpecs.add(fieldSpec);
        }
        return fieldSpecs;
    }

    private MethodSpec createMarshallerGetSizeMethod(TypeElement element, List<Property> properties) {
        final MethodSpec.Builder getSize = MethodSpec.methodBuilder("getSize").addModifiers(Modifier.PROTECTED)
                .addParameter(TypeName.get(element.asType()), "target").returns(int.class);

        getSize.addStatement("int mapSize = $L", getNonNullCount(properties));

        final Access.AccessType accessType = getAccessType(element);
        for (final Property property : properties) {
            if (!property.getType().getKind().isPrimitive()) {
                if (Access.AccessType.FIELD.equals(accessType)) {
                    getSize.beginControlFlow("if (target.$N != null)", property.getName());
                } else {
                    getSize.beginControlFlow("if (target.get$N() != null)", Utils.capitalize(property.getName()));
                }
                getSize.addStatement("mapSize++");
                getSize.endControlFlow();
            }
        }
        getSize.addStatement("return mapSize");

        return getSize.build();
    }

    private MethodSpec createMarshallerMarshalContentMethod(TypeElement element, List<Property> properties) {
        final MethodSpec.Builder marshalContent = MethodSpec.methodBuilder("marshalContent")
                .addModifiers(Modifier.PROTECTED).addParameter(MessagePacker.class, "packer")
                .addParameter(TypeName.get(element.asType()), "target").addException(IOException.class);

        final Access.AccessType accessType = getAccessType(element);
        for (final Property property : properties) {
            final TypeMirror type = property.getType();

            if (!type.getKind().isPrimitive()) {
                if (Access.AccessType.FIELD.equals(accessType)) {
                    marshalContent.beginControlFlow("if (target.$N != null)", property.getName());
                } else {
                    marshalContent.beginControlFlow("if (target.get$N() != null)", Utils.capitalize(property.getName()));
                }
            }
            marshalContent.addStatement("packer.packString($S)", property.getName());
            if (Access.AccessType.FIELD.equals(accessType)) {
                marshalContent.addStatement("$N.marshal(packer, target.$N)",
                        property.getName() + Marshallers.SUFFIX_MARSHALLER, property.getName());
            } else {
                marshalContent.addStatement("$N.marshal(packer, target.get$N())",
                        property.getName() + Marshallers.SUFFIX_MARSHALLER, Utils.capitalize(property.getName()));
            }
            if (!type.getKind().isPrimitive()) {
                marshalContent.endControlFlow();
            }
        }

        return marshalContent.build();
    }

    private int getNonNullCount(List<Property> properties) {
        int nonNullCount = 0;
        for (final Property property : properties) {
            if (property.getType().getKind().isPrimitive()) {
                nonNullCount++;
            }
        }
        return nonNullCount;
    }

    private List<FieldSpec> createUnmarshallerFields(TypeElement element, List<Property> properties) {
        final List<FieldSpec> fieldSpecs = new ArrayList<>(properties.size());

        for (final Property property : properties) {
            TypeMirror propertyType = property.getType();

            if (propertyType.getKind().isPrimitive()) {
                propertyType = types.boxedClass((PrimitiveType) propertyType).asType();
            }

            final DeclaredType newFieldType = types.getDeclaredType(elements
                    .getTypeElement(Unmarshaller.class.getName()), propertyType);
            final FieldSpec fieldSpec = FieldSpec.builder(TypeName.get(newFieldType),
                    property.getName() + Unmarshallers.SUFFIX_UNMARSHALLER, Modifier.PRIVATE, Modifier.FINAL)
                    .initializer(Unmarshallers.getUnmarshaller(types, elements, propertyType))
                    .build();

            fieldSpecs.add(fieldSpec);
        }

        return fieldSpecs;
    }

    private MethodSpec createUnmarshallerUnmarshalPropertyMethod(TypeElement element, List<Property> properties) {
        final MethodSpec.Builder unmarshalProperty = MethodSpec.methodBuilder("unmarshalProperty")
                .addModifiers(Modifier.PROTECTED).addParameter(MessageUnpacker.class, "unpacker")
                .addParameter(TypeName.get(element.asType()), "target").addParameter(String.class, "propertyName")
                .returns(boolean.class).addException(IOException.class);

        final Access.AccessType accessType = getAccessType(element);

        unmarshalProperty.beginControlFlow("switch(propertyName)");
        for (final Property property : properties) {
            unmarshalProperty.beginControlFlow("case $S:", property.getName());
            if (Access.AccessType.FIELD.equals(accessType)) {
                unmarshalProperty.addStatement("target.$N = $NUnmarshaller.unmarshal(unpacker)", property.getName(),
                        property.getName());
            } else {
                unmarshalProperty.addStatement("target.set$N($NUnmarshaller.unmarshal(unpacker))",
                        Utils.capitalize(property.getName()), property.getName());
            }
            unmarshalProperty.addStatement("return true");
            unmarshalProperty.endControlFlow();
        }
        unmarshalProperty.endControlFlow();

        unmarshalProperty.addStatement("return false");

        return unmarshalProperty.build();
    }

    private MethodSpec createNewInstanceMethod(TypeElement element) {
        final MethodSpec.Builder newInstance = MethodSpec.methodBuilder("newInstance").addModifiers(Modifier.PROTECTED)
                .returns(TypeName.get(element.asType()));

        newInstance.addStatement("return new $T()", element);

        return newInstance.build();
    }

    private List<Property> getFields(final TypeElement element) {
        final List<? extends Element> allMembers = elements.getAllMembers(element);
        final List<Property> properties = new ArrayList<>(allMembers.size());
        for (final Element member : allMembers) {
            final Set<Modifier> modifiers = member.getModifiers();
            if (member.getKind().isField() && modifiers.contains(Modifier.PUBLIC)
                    && !modifiers.contains(Modifier.STATIC) && !modifiers.contains(Modifier.FINAL)) {
                // public instance field only
                final Property property = new Property();
                property.setName(member.getSimpleName().toString());
                property.setType(member.asType());
                property.setGettable(true);
                property.setSettable(!modifiers.contains(Modifier.FINAL));
                properties.add(property);
            }
        }

        if (DEBUG) {
            System.out.println("fields=" + properties);
        }

        return properties;
    }

    private boolean isGetter(ExecutableElement method) {
        final String methodName = method.getSimpleName().toString();
        return methodName.startsWith(GETTER_PREFIX) && methodName.length() > GETTER_PREFIX.length()
                && method.getParameters().size() == 0
                && method.getReturnType().getKind() != TypeKind.VOID;
    }

    private boolean isSetter(ExecutableElement method) {
        final String methodName = method.getSimpleName().toString();
        return methodName.startsWith(SETTER_PREFIX) && methodName.length() > SETTER_PREFIX.length()
                && method.getParameters().size() == 1
                && method.getReturnType().getKind() == TypeKind.VOID;
    }

    private boolean isGenerateMarshaller(MsgpackBind.GenerateType generateType) {
        return MsgpackBind.GenerateType.BOTH.equals(generateType)
                || MsgpackBind.GenerateType.MARSHALLER.equals(generateType);
    }

    private boolean isGenerateUnmarshaller(MsgpackBind.GenerateType generateType) {
        return MsgpackBind.GenerateType.BOTH.equals(generateType)
                || MsgpackBind.GenerateType.UNMARSHALLER.equals(generateType);
    }

    private List<Property> getProperties(final TypeElement element) {
        final List<? extends Element> allMembers = elements.getAllMembers(element);
        final Map<String, Property> map = new HashMap<>(allMembers.size() * 2);
        final MsgpackBind.GenerateType generateType = element.getAnnotation(MsgpackBind.class).value();
        for (final Element member : allMembers) {
            final ElementKind memberKind = member.getKind();
            final Set<Modifier> modifiers = member.getModifiers();
            if (!memberKind.isField() && !memberKind.isClass() && !memberKind.isInterface()
                    && modifiers.contains(Modifier.PUBLIC) && !modifiers.contains(Modifier.STATIC)) {

                if (Utils.isSameType(types, elements, member.getEnclosingElement().asType(), Object.class)) {
                    // ignore Object method
                    continue;
                }

                // public instance method only
                String name = member.getSimpleName().toString();

                TypeMirror type = null;
                final ExecutableElement method = (ExecutableElement) member;
                boolean isSetter = false;
                if (isSetter(method) && isGenerateUnmarshaller(generateType)) {
                    name = Utils.decapitalize(name.substring(SETTER_PREFIX.length()));
                    type = method.getParameters().get(0).asType();
                    isSetter = true;
                } else if (isGetter(method) && isGenerateMarshaller(generateType)) {
                    name = Utils.decapitalize(name.substring(GETTER_PREFIX.length()));
                    type = method.getReturnType();
                    isSetter = false;
                }

                if (type != null) {
                    Property property = map.get(name);
                    if (property == null) {
                        property = new Property();
                        property.setName(name);
                        property.setType(type);
                        map.put(name, property);
                    } else {
                        if (!types.isSameType(property.getType(), type)) {
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                                    "Can't decide property type. property:" + property + ", member:" + member, element);
                        }
                    }

                    if (isSetter) {
                        property.setSettable(true);
                    } else {
                        property.setGettable(true);
                    }
                }
            }
        }

        final List<Property> properties = new ArrayList<>(map.values());
        if (DEBUG) {
            System.out.println("properties=" + properties);
        }

        if (MsgpackBind.GenerateType.BOTH.equals(generateType)) {
            // check if getter and setter is paired.
            final Iterator<Property> it = properties.iterator();
            while (it.hasNext()) {
                final Property property = it.next();
                if (!property.isGettable()) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                            "Cannot get property:" + property, element);
                    it.remove();
                } else if (!property.isSettable()) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                            "Cannot set property:" + property, element);
                    it.remove();
                }
            }
        }

        return properties;
    }

    private static class Property {
        private TypeMirror type;
        private String name;
        private boolean settable;
        private boolean gettable;

        public TypeMirror getType() {
            return type;
        }

        public void setType(TypeMirror type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSettable() {
            return settable;
        }

        public void setSettable(boolean settable) {
            this.settable = settable;
        }

        public boolean isGettable() {
            return gettable;
        }

        public void setGettable(boolean gettable) {
            this.gettable = gettable;
        }

        @Override
        public String toString() {
            return "{\n" + "\ttype:" + type + ",\n\tname:" + name + ",\n\tsettable:" + settable
                    + ",\n\tgettable:" + gettable + "\n}";
        }
    }
}
