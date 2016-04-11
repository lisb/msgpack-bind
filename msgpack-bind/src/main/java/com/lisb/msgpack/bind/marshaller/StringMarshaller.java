package com.lisb.msgpack.bind.marshaller;

import org.msgpack.core.MessagePacker;

import java.io.IOException;

public class StringMarshaller extends AbstractMarshaller<String> {

    private static final StringMarshaller instance = new StringMarshaller();

    public static StringMarshaller getInstance() {
        return instance;
    }

    private StringMarshaller() {
    }

    @Override
    public void _marshal(MessagePacker packer, String target) throws IOException {
        packer.packString(target);
    }
}
