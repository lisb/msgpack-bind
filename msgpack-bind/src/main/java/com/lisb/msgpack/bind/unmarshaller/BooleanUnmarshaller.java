package com.lisb.msgpack.bind.unmarshaller;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class BooleanUnmarshaller extends AbstractUnmarshaller<Boolean> {

    private static final BooleanUnmarshaller instance = new BooleanUnmarshaller();

    public static BooleanUnmarshaller getInstance() {
        return instance;
    }

    private BooleanUnmarshaller() {
    }

    @Override
    public Boolean _unmarshal(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackBoolean();
    }
}
