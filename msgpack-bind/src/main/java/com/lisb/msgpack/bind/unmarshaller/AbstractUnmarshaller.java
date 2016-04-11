package com.lisb.msgpack.bind.unmarshaller;

import com.lisb.msgpack.bind.Unmarshaller;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public abstract class AbstractUnmarshaller<T> implements Unmarshaller<T> {
    @Override
    public final T unmarshal(MessageUnpacker unpacker) throws IOException {
        if (unpacker.getNextFormat().getValueType().isNilType()) {
            unpacker.skipValue();
            return null;
        } else {
            return _unmarshal(unpacker);
        }
    }

    public abstract T _unmarshal(MessageUnpacker unpacker) throws IOException;
}
