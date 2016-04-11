package com.lisb.msgpack.bind;

import org.msgpack.core.MessagePacker;

import java.io.IOException;

public interface Marshaller<T> {
    void marshal(MessagePacker packer, T target) throws IOException;
}
