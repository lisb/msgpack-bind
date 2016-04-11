package com.lisb.msgpack.bind.marshaller;

import org.msgpack.core.MessagePacker;

import java.io.IOException;

public class DoubleMarshaller extends AbstractMarshaller<Double> {

    private static final DoubleMarshaller instance = new DoubleMarshaller();

    public static DoubleMarshaller getInstance() {
        return instance;
    }

    private DoubleMarshaller() {
    }

    @Override
    protected void _marshal(MessagePacker packer, Double target) throws IOException {
        packer.packDouble(target);
    }
}
