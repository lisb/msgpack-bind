package com.lisb.msgpack.bind.marshaller;

import org.msgpack.core.MessagePacker;

import java.io.IOException;

public class ByteMarshaller extends AbstractMarshaller<Byte> {

    private static final ByteMarshaller instance = new ByteMarshaller();

    public static ByteMarshaller getInstance() {
        return instance;
    }

    private ByteMarshaller() {
    }

    @Override
    protected void _marshal(MessagePacker packer, Byte target) throws IOException {
        packer.packByte(target);
    }
}
