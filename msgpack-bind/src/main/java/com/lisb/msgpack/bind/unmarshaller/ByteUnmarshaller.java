package com.lisb.msgpack.bind.unmarshaller;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class ByteUnmarshaller extends AbstractUnmarshaller<Byte> {

    private static final ByteUnmarshaller instance = new ByteUnmarshaller();

    public static ByteUnmarshaller getInstance() {
        return instance;
    }

    private ByteUnmarshaller() {
    }

    @Override
    public Byte _unmarshal(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackByte();
    }
}
