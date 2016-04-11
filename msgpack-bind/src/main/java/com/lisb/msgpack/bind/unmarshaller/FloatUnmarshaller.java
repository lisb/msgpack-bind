package com.lisb.msgpack.bind.unmarshaller;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class FloatUnmarshaller extends AbstractUnmarshaller<Float> {

    private static final FloatUnmarshaller instance = new FloatUnmarshaller();

    public static FloatUnmarshaller getInstance() {
        return instance;
    }

    private FloatUnmarshaller() {
    }

    @Override
    public Float _unmarshal(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackFloat();
    }
}
