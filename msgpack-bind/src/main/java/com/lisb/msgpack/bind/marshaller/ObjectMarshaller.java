package com.lisb.msgpack.bind.marshaller;

import org.msgpack.core.MessagePacker;

import java.io.IOException;

public class ObjectMarshaller<T> extends AbstractMarshaller<T> {

    @Override
    protected final void _marshal(MessagePacker packer, T target) throws IOException {
        packer.packMapHeader(getSize(target));
        marshalContent(packer, target);
    }

    protected int getSize(T target) {
        return 0;
    }

    protected void marshalContent(MessagePacker packer, T target) throws IOException {
    }
}
