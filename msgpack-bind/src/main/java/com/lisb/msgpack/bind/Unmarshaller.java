package com.lisb.msgpack.bind;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public interface Unmarshaller<T> {
    T unmarshal(MessageUnpacker unpacker) throws IOException;
}
