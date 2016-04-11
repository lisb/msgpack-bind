package com.lisb.msgpack.bind;

import com.lisb.msgpack.bind.marshaller.IntegerMarshaller;
import com.lisb.msgpack.bind.marshaller.ObjectMarshaller;
import java.io.IOException;
import java.lang.Integer;
import org.msgpack.core.MessagePacker;

public class PackagedSourceMarshaller extends ObjectMarshaller<PackagedSource> {

    private final Marshaller<Integer> firstMarshaller = IntegerMarshaller.getInstance();

    protected int getSize(PackagedSource target) {
        int mapSize = 1;
        return mapSize;
    }

    protected void marshalContent(MessagePacker packer, PackagedSource target) throws IOException {
        packer.packString("first");
        firstMarshaller.marshal(packer, target.first);
    }
}