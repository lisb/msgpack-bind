package com.lisb.msgpack.bind.unmarshaller;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class DoubleUnmarshaller extends AbstractUnmarshaller<Double> {

    private static final DoubleUnmarshaller instance = new DoubleUnmarshaller();

    public static DoubleUnmarshaller getInstance() {
        return instance;
    }

    private DoubleUnmarshaller() {
    }

    @Override
    public Double _unmarshal(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackDouble();
    }
}
