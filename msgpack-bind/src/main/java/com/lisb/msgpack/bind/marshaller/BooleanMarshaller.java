package com.lisb.msgpack.bind.marshaller;

import org.msgpack.core.MessagePacker;

import java.io.IOException;

public class BooleanMarshaller extends AbstractMarshaller<Boolean> {

    private static final BooleanMarshaller instance = new BooleanMarshaller();

    public static BooleanMarshaller getInstance() {
        return instance;
    }

    private BooleanMarshaller() {
    }

    @Override
    public void _marshal(MessagePacker packer, Boolean target) throws IOException {
        packer.packBoolean(target);
    }
}
