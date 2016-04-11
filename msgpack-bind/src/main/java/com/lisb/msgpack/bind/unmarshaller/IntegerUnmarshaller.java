package com.lisb.msgpack.bind.unmarshaller;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class IntegerUnmarshaller extends AbstractUnmarshaller<Integer> {

    private static final IntegerUnmarshaller instance = new IntegerUnmarshaller();

    public static IntegerUnmarshaller getInstance() {
        return instance;
    }

    private IntegerUnmarshaller() {
    }

    @Override
    public Integer _unmarshal(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackInt();
    }
}
