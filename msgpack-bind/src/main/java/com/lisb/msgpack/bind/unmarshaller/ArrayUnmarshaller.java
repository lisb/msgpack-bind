package com.lisb.msgpack.bind.unmarshaller;

import com.lisb.msgpack.bind.Unmarshaller;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.lang.reflect.Array;

public class ArrayUnmarshaller<E> extends AbstractUnmarshaller<E[]> {

    private final Class<E> clz;
    private final Unmarshaller<E> elementUnmarshaller;

    public ArrayUnmarshaller(Class<E> clz, Unmarshaller<E> elementUnmarshaller) {
        this.clz = clz;
        this.elementUnmarshaller = elementUnmarshaller;
    }

    @Override
    public E[] _unmarshal(MessageUnpacker unpacker) throws IOException {
        final int size = unpacker.unpackArrayHeader();
        final E[] result = (E[]) Array.newInstance(clz, size);
        for (int i = 0; i < size; i++) {
            result[i] = elementUnmarshaller.unmarshal(unpacker);
        }
        return result;
    }
}
