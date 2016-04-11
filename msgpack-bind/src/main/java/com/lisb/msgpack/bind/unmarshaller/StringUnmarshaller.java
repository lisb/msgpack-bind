package com.lisb.msgpack.bind.unmarshaller;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class StringUnmarshaller extends AbstractUnmarshaller<String> {

    private static final StringUnmarshaller instance = new StringUnmarshaller();

    public static StringUnmarshaller getInstance() {
        return instance;
    }

    private StringUnmarshaller() {
    }

    @Override
    public String _unmarshal(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackString();
    }
}
