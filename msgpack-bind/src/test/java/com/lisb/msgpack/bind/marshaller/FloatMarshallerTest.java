package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.BooleanUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.FloatUnmarshaller;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

public class FloatMarshallerTest {

    private static Marshaller<Float> marshaller;
    private static Unmarshaller<Float> unmarshaller;

    @BeforeClass
    public static void beforeClass() {
        marshaller = FloatMarshaller.getInstance();
        unmarshaller = FloatUnmarshaller.getInstance();
    }

    @Test
    public void testMarshalAndUnmarshal() throws Exception {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        final Float source = 9609.6585f;

        marshaller.marshal(packer, source);

        final Float dest = unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray()));

        Assert.assertEquals(source, dest);
    }

    @Test
    public void testNullBMarshalAndUnmarshal() throws Exception {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        marshaller.marshal(packer, null);
        Assert.assertNull(unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray())));
    }
}
