package com.lisb.msgpack.bind.marshaller;

import org.msgpack.core.MessagePacker;

import java.io.IOException;

public class EnumMarshaller<E extends Enum<E>> extends AbstractMarshaller<E> {
    @Override
    protected void _marshal(MessagePacker packer, E target) throws IOException {
        packer.packString(target.name());
    }
}
