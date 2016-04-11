package com.lisb.msgpack.bind.marshaller;

import org.msgpack.core.MessagePacker;

import java.io.IOException;

public class IntegerMarshaller extends AbstractMarshaller<Integer> {

    private static final IntegerMarshaller instance = new IntegerMarshaller();

    public static IntegerMarshaller getInstance() {
        return instance;
    }

    private IntegerMarshaller() {
    }

    @Override
    protected void _marshal(MessagePacker packer, Integer target) throws IOException {
        packer.packInt(target);
    }
}
