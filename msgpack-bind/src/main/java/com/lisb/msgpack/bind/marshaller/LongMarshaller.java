package com.lisb.msgpack.bind.marshaller;

import org.msgpack.core.MessagePacker;

import java.io.IOException;

public class LongMarshaller extends AbstractMarshaller<Long> {

    private static final LongMarshaller instance = new LongMarshaller();

    public static LongMarshaller getInstance() {
        return instance;
    }

    private LongMarshaller() {
    }

    @Override
    protected void _marshal(MessagePacker packer, Long target) throws IOException {
        packer.packLong(target);
    }
}
