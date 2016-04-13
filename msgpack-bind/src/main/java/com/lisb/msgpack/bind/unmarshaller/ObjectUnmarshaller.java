package com.lisb.msgpack.bind.unmarshaller;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class ObjectUnmarshaller<T> extends AbstractUnmarshaller<T> {

    @Override
    public T _unmarshal(MessageUnpacker unpacker) throws IOException {
        final T obj = newInstance();
        final int mapSize = unpacker.unpackMapHeader();
        for (int i = 0; i < mapSize; i++) {
            final String key = unpacker.unpackString();
            if (!unmarshalProperty(unpacker, obj, key)) {
                unpacker.skipValue();
            }
        }
        return obj;
    }

    protected boolean unmarshalProperty(MessageUnpacker unpacker, T target, String propertyName) throws IOException {
        return false;
    }

    protected T newInstance() {
        return null;
    }
}
