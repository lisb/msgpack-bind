package com.lisb.msgpack.bind.marshaller;

import org.msgpack.core.MessagePacker;

import java.io.IOException;

public class ShortMarshaller extends AbstractMarshaller<Short> {

    private static final ShortMarshaller instance = new ShortMarshaller();

    public static ShortMarshaller getInstance() {
        return instance;
    }

    private ShortMarshaller() {
    }

    @Override
    protected void _marshal(MessagePacker packer, Short target) throws IOException {
        packer.packShort(target);
    }
}
