package com.lisb.msgpack.bind.unmarshaller;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class EnumUnmarshaller<E extends Enum<E>> extends AbstractUnmarshaller<E> {

    private final Class<E> clz;

    public EnumUnmarshaller(Class<E> clz) {
        this.clz = clz;
    }

    @Override
    public E _unmarshal(MessageUnpacker unpacker) throws IOException {
        return Enum.valueOf(clz, unpacker.unpackString());
    }
}
