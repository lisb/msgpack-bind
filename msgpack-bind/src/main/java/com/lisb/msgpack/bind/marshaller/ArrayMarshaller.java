package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import org.msgpack.core.MessagePacker;

import java.io.IOException;

public class ArrayMarshaller<E> extends AbstractMarshaller<E[]> {

    private final Marshaller<E> elementMarshaller;

    public ArrayMarshaller(Marshaller<E> elementMarshaller) {
        this.elementMarshaller = elementMarshaller;
    }

    @Override
    protected void _marshal(MessagePacker packer, E[] target) throws IOException {
        packer.packArrayHeader(target.length);
        for (final E element : target) {
            elementMarshaller.marshal(packer, element);
        }
    }
}
