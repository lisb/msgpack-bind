package com.lisb.msgpack.bind.example;

import com.lisb.msgpack.bind.MsgpackBind;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Example {

    public static void main(String[] args) {
        try {
            final Example example = new Example();

            final Simple source = new Simple();
            final byte[] bytes = example.marshal(source);
            final Simple dest = example.unmarshal(bytes);

            System.out.println("source:" + source);
            System.out.println("dest:" + dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] marshal(Simple target) throws IOException {
        target.second = new ArrayList<>();
        target.second.add("1");
        target.second.add("2");

        target.third = new ArrayList<>();
        final List<String> thirdFirstComponent = new LinkedList<>();
        thirdFirstComponent.add("1_1");
        thirdFirstComponent.add("1_2");
        thirdFirstComponent.add(null);
        target.third.add(thirdFirstComponent);

        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        new Example$SimpleMarshaller().marshal(packer, target);
        packer.close();

        return packer.toByteArray();
    }

    private Simple unmarshal(byte[] bytes) throws IOException {
        final MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(bytes);
        final Simple target = new Example$SimpleUnmarshaller().unmarshal(unpacker);
        unpacker.close();
        return target;
    }

    @MsgpackBind
    public static class Simple {
        public Boolean first;
        public List<String> second;
        public List<List<String>> third;

        @Override
        public String toString() {
            return "{\n\tfirst:" + first + ",\n\tsecond:" + second + ",\n\tthird:" + third + "\n}";
        }
    }
}
