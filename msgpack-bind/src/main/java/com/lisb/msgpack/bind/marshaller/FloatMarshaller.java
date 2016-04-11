package com.lisb.msgpack.bind.marshaller;

import org.msgpack.core.MessagePacker;

import java.io.IOException;

public class FloatMarshaller extends AbstractMarshaller<Float> {

    private static final FloatMarshaller instance = new FloatMarshaller();

    public static FloatMarshaller getInstance() {
        return instance;
    }

    private FloatMarshaller() {
    }

    @Override
    protected void _marshal(MessagePacker packer, Float target) throws IOException {
        packer.packFloat(target);
    }
}
