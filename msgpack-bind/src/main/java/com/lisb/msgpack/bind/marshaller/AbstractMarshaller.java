package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import org.msgpack.core.MessagePacker;

import java.io.IOException;

public abstract class AbstractMarshaller<T> implements Marshaller<T> {

    @Override
    public final void marshal(MessagePacker packer, T target) throws IOException {
        if (target == null) {
            packer.packNil();
        } else {
            _marshal(packer, target);
        }
    }

    protected abstract void _marshal(MessagePacker packer, T target) throws IOException;
}
