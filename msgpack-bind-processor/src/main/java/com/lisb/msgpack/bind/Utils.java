package com.lisb.msgpack.bind;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
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

    public static String getBaseClassName(Element element) {
        final StringBuilder className = new StringBuilder(element.getSimpleName());
        Element enclosingElement = element.getEnclosingElement();
        while (enclosingElement instanceof TypeElement) {
            className.insert(0, enclosingElement.getSimpleName() + "$");
            enclosingElement = enclosingElement.getEnclosingElement();
        }
        return className.toString();
    }
}
