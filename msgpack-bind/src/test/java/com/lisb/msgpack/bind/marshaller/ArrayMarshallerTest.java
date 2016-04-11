package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.ArrayUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.IntegerUnmarshaller;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

public class ArrayMarshallerTest {
    private static Marshaller<Integer[]> marshaller;
    private static Unmarshaller<Integer[]> unmarshaller;

    @BeforeClass
    public static void beforeClass() {
        marshaller = new ArrayMarshaller<Integer>(IntegerMarshaller.getInstance());
        unmarshaller = new ArrayUnmarshaller<Integer>(Integer.class, IntegerUnmarshaller.getInstance());
    }

    @Test
    public void testMarshalAndUnmarshal() throws Throwable {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        final Integer[] source = new Integer[3];
        source[0] = 6545;
        source[2] = 165;

        marshaller.marshal(packer, source);

        final Integer[] dest = unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray()));

        Assert.assertArrayEquals(source, dest);
    }

    @Test
    public void testNullMarshalAndUnmarshal() throws Throwable {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        marshaller.marshal(packer, null);

        Assert.assertNull(unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray())));
    }
}
