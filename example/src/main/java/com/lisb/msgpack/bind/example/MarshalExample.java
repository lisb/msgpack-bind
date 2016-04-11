package com.lisb.msgpack.bind.example;

import com.lisb.msgpack.bind.MsgpackBind;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MarshalExample {

    public static void main(String[] args) {
        try {
            new MarshalExample().simpleMarshal();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void simpleMarshal() throws IOException {
        final Simple target = new Simple();

        target.second = new ArrayList<>();
        target.second.add("1");
        target.second.add("2");

        target.third = new ArrayList<>();
        target.third.add(new LinkedList<String>());

        final MarshalExample$SimpleMarshaller marshaller = new MarshalExample$SimpleMarshaller();

        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        marshaller.marshal(packer, target);
        packer.close();
    }

    @MsgpackBind
    public static class Simple {
        public Boolean first;
        public List<String> second;
        public List<List<String>> third;
    }
}
