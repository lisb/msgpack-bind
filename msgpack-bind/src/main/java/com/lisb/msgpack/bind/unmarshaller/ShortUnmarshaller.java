package com.lisb.msgpack.bind.unmarshaller;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class ShortUnmarshaller extends AbstractUnmarshaller<Short> {

    private static final ShortUnmarshaller instance = new ShortUnmarshaller();

    public static ShortUnmarshaller getInstance() {
        return instance;
    }

    private ShortUnmarshaller() {
    }

    @Override
    public Short _unmarshal(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackShort();
    }
}
