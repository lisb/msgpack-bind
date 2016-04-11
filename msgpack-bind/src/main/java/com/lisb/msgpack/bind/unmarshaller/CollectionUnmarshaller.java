package com.lisb.msgpack.bind.unmarshaller;

import com.lisb.msgpack.bind.Unmarshaller;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class CollectionUnmarshaller<E, T extends Collection<E>> extends AbstractUnmarshaller<T> {

    private final CollectionFactory<E, T> collectionFactory;
    private final Unmarshaller<E> elementUnmarshaller;

    public CollectionUnmarshaller(CollectionFactory<E, T> collectionFactory, Unmarshaller<E> elementUnmarshaller) {
        this.collectionFactory = collectionFactory;
        this.elementUnmarshaller = elementUnmarshaller;
    }

    @Override
    public T _unmarshal(MessageUnpacker unpacker) throws IOException {
        final int size = unpacker.unpackArrayHeader();
        final T collection = collectionFactory.createCollection(size);
        for (int i = 0; i < size; i++) {
            collection.add(elementUnmarshaller.unmarshal(unpacker));
        }
        return collection;
    }

    interface CollectionFactory<E, T extends Collection<E>> {
        T createCollection(int size);
    }

    public static class ArrayListUnmarshaller<E> extends CollectionUnmarshaller<E, ArrayList<E>>{
        public ArrayListUnmarshaller(Unmarshaller<E> elementUnmarshaller) {
            super(new CollectionFactory<E, ArrayList<E>>() {
                @Override
                public ArrayList<E> createCollection(int size) {
                    return new ArrayList<E>(size);
                }
            }, elementUnmarshaller);
        }
    }

    public static class HashSetUnmarshaller<E> extends CollectionUnmarshaller<E, HashSet<E>> {
        public HashSetUnmarshaller(Unmarshaller<E> elementUnmarshaller) {
            super(new CollectionFactory<E, HashSet<E>>() {
                @Override
                public HashSet<E> createCollection(int size) {
                    return new HashSet<E>(size * 2);
                }
            }, elementUnmarshaller);
        }
    }
}
