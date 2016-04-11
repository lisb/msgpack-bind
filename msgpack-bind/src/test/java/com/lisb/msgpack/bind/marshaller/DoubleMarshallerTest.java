package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.DoubleUnmarshaller;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

public class DoubleMarshallerTest {

    private static Marshaller<Double> marshaller;
    private static Unmarshaller<Double> unmarshaller;

    @BeforeClass
    public static void beforeClass() {
        marshaller = DoubleMarshaller.getInstance();
        unmarshaller = DoubleUnmarshaller.getInstance();
    }

    @Test
    public void testMarshalAndUnmarshal() throws Exception {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        final Double source = 654743534d;

        marshaller.marshal(packer, source);

        final Double dest = unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray()));

        Assert.assertEquals(source, dest);
    }

    @Test
    public void testNullBMarshalAndUnmarshal() throws Exception {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        marshaller.marshal(packer, null);
        Assert.assertNull(unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray())));
    }
}
