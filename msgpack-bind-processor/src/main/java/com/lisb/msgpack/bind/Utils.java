package com.lisb.msgpack.bind;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Arrays;
import java.util.List;

class Utils {

    private static final boolean DEBUG = false;

    private Utils() {
    }

    public static TypeMirror getInterface(Types types, TypeMirror type, TypeElement interfaceElement) {
        while (type.getKind() != TypeKind.NONE) {
            final List<? extends TypeMirror> superTypes = types.directSupertypes(type);
            type = superTypes.get(0);
            for (int i = 1, size = superTypes.size(); i < size; i++) {
                final TypeMirror interfaceMirror = superTypes.get(i);
                if (types.asElement(interfaceMirror).equals(interfaceElement)) {
                    if (DEBUG) {
                        System.out.println("interface is " + interfaceMirror);
                    }
                    return interfaceMirror;
                }
            }
        }

        throw new IllegalArgumentException("typeElement is not " + interfaceElement);
    }

    public static TypeMirror asType(Types types, Elements elements, Class<?> clz) {
        final TypeElement typeElement = elements.getTypeElement(clz.getName());

        final int typeParametersSize = typeElement.getTypeParameters().size();
        final TypeMirror type;
        if (typeParametersSize == 0) {
            type = typeElement.asType();
        } else {
            // erase type parameter
            type = types.getDeclaredType(typeElement);
        }
        return type;
    }

    public static boolean isSameType(Types types, Elements elements, TypeMirror target, Class<?> comparisonClass) {
        return types.isSameType(target, asType(types, elements, comparisonClass));
    }

    public static String getBaseClassName(Element element) {
        final StringBuilder className = new StringBuilder(element.getSimpleName());
        Element enclosingElement = element.getEnclosingElement();
        while (enclosingElement instanceof TypeElement) {
            className.insert(0, enclosingElement.getSimpleName() + "$");
            enclosingElement = enclosingElement.getEnclosingElement();
        }
        return className.toString();
    }

    public static String decapitalize(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        final char c[] = string.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    public static String capitalize(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        final char c[] = string.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }
}
