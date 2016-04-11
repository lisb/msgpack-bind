package com.lisb.msgpack.bind;

import com.lisb.msgpack.bind.marshaller.ObjectMarshaller;
import com.squareup.javapoet.*;
import org.msgpack.core.MessagePacker;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("com.lisb.msgpack.bind.MsgpackBind")
public class MsgpackBindProcessor extends AbstractProcessor {

    private static final boolean DEBUG = false;

    private Types types;
    private Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elements = processingEnv.getElementUtils();
        this.types = processingEnv.getTypeUtils();
    }
//
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return SourceVersion.latestSupported();
//    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // TODO generate Unmarshaller
        for (final Element element : roundEnv.getElementsAnnotatedWith(MsgpackBind.class)) {
            final TypeElement typeElement = (TypeElement) element;

            final List<Element> fields = getFields(typeElement);

            final String baseClassName = Utils.getBaseClassName(typeElement);
            final String packageName = elements.getPackageOf(typeElement).getQualifiedName().toString();

            final DeclaredType supperClass = types.getDeclaredType(
                    elements.getTypeElement(ObjectMarshaller.class.getName()), typeElement.asType());

            final TypeSpec marshaller = TypeSpec.classBuilder(baseClassName + Marshallers.SUFFIX_MARSHALLER)
                    .addModifiers(Modifier.PUBLIC).superclass(TypeName.get(supperClass))
                    .addFields(createFields(typeElement, fields))
                    .addMethod(createGetSizeMethod(typeElement, fields))
                    .addMethod(createMarshalContentMethod(typeElement, fields))
                    .build();

            final JavaFile javaFile = JavaFile.builder(packageName, marshaller).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());

                if (DEBUG) {
                    javaFile.writeTo(System.out);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    private List<FieldSpec> createFields(TypeElement element, List<Element> fields) {
        final List<FieldSpec> fieldSpecs = new ArrayList<>(fields.size());
        for (final Element field : fields) {
            TypeMirror fieldType = field.asType();

            if (fieldType.getKind().isPrimitive()) {
                fieldType = types.boxedClass((PrimitiveType) fieldType).asType();
            }

            final DeclaredType newFieldType = types.getDeclaredType(elements
                    .getTypeElement(Marshaller.class.getName()), fieldType);
            final FieldSpec fieldSpec = FieldSpec.builder(TypeName.get(newFieldType),
                    field.getSimpleName() + Marshallers.SUFFIX_MARSHALLER, Modifier.PRIVATE, Modifier.FINAL)
                    .initializer(Marshallers.getMarshaller(types, elements, fieldType))
                    .build();
            fieldSpecs.add(fieldSpec);
        }
        return fieldSpecs;
    }

    private MethodSpec createGetSizeMethod(TypeElement element, List<Element> fields) {
        final MethodSpec.Builder getSize = MethodSpec.methodBuilder("getSize").addModifiers(Modifier.PROTECTED)
                .addParameter(TypeName.get(element.asType()), "target").returns(int.class);

        getSize.addStatement("int mapSize = $L", getNonNullCount(fields));

        for (final Element field : fields) {
            final TypeMirror type = field.asType();
            if (!type.getKind().isPrimitive()) {
                getSize.beginControlFlow("if (target.$N != null)", field.getSimpleName());
                getSize.addStatement("mapSize++");
                getSize.endControlFlow();
            }
        }
        getSize.addStatement("return mapSize");

        return getSize.build();
    }

    private MethodSpec createMarshalContentMethod(TypeElement element, List<Element> fields) {
        final MethodSpec.Builder marshalContent = MethodSpec.methodBuilder("marshalContent")
                .addModifiers(Modifier.PROTECTED).addParameter(MessagePacker.class, "packer")
                .addParameter(TypeName.get(element.asType()), "target").addException(IOException.class);

        for (final Element field : fields) {
            final TypeMirror type = field.asType();

            if (!type.getKind().isPrimitive()) {
                marshalContent.beginControlFlow("if (target.$N != null)", field.getSimpleName());
            }
            marshalContent.addStatement("packer.packString($S)", field.getSimpleName());
            marshalContent.addStatement("$N.marshal(packer, target.$N)",
                    field.getSimpleName() + Marshallers.SUFFIX_MARSHALLER, field.getSimpleName());
            if (!type.getKind().isPrimitive()) {
                marshalContent.endControlFlow();
            }
        }

        return marshalContent.build();
    }

    private int getNonNullCount(List<Element> fields) {
        int nonNullCount = 0;
        for (final Element field : fields) {
            final TypeMirror type = field.asType();
            if (type.getKind().isPrimitive()) {
                nonNullCount++;
            }
        }
        return nonNullCount;
    }

    private List<Element> getFields(final TypeElement element) {
        final List<? extends Element> allMembers = elements.getAllMembers(element);
        final List<Element> fields = new ArrayList<>(allMembers.size());
        for (final Element member : allMembers) {
            if (member.getKind().isField() && member.getModifiers().contains(Modifier.PUBLIC)) {
                // public field only
                fields.add(member);
            }
        }

        if (DEBUG) {
            System.out.println("fields=" + fields);
        }

        return fields;
    }
}
