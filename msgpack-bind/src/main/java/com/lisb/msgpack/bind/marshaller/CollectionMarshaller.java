package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import org.msgpack.core.MessagePacker;

import java.io.IOException;
import java.util.Collection;

public class CollectionMarshaller<E> extends AbstractMarshaller<Collection<E>> {

    private final Marshaller<E> elementMarshaller;

    public CollectionMarshaller(Marshaller<E> elementMarshaller) {
        this.elementMarshaller = elementMarshaller;
    }

    @Override
    public void _marshal(MessagePacker packer, Collection<E> target) throws IOException {
        packer.packArrayHeader(target.size());
        for (final E element : target) {
            elementMarshaller.marshal(packer, element);
        }
    }
}
