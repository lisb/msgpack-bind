package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.ShortUnmarshaller;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

public class ShortMarshallerTest {

    private static Marshaller<Short> marshaller;
    private static Unmarshaller<Short> unmarshaller;

    @BeforeClass
    public static void beforeClass() {
        marshaller = ShortMarshaller.getInstance();
        unmarshaller = ShortUnmarshaller.getInstance();
    }

    @Test
    public void testMarshalAndUnmarshal() throws Exception {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        final Short source = 654;

        marshaller.marshal(packer, source);

        final Short dest = unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray()));

        Assert.assertEquals(source, dest);
    }

    @Test
    public void testNullBMarshalAndUnmarshal() throws Exception {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        marshaller.marshal(packer, null);
        Assert.assertNull(unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray())));
    }
}
