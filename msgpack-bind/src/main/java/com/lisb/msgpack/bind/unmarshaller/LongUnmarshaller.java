package com.lisb.msgpack.bind.unmarshaller;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class LongUnmarshaller extends AbstractUnmarshaller<Long> {

    private static final LongUnmarshaller instance = new LongUnmarshaller();

    public static LongUnmarshaller getInstance() {
        return instance;
    }

    private LongUnmarshaller() {
    }

    @Override
    public Long _unmarshal(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackLong();
    }
}
