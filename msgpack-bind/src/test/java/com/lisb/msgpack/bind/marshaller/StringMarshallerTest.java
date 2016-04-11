package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.StringUnmarshaller;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

public class StringMarshallerTest {

    private static Marshaller<String> marshaller;
    private static Unmarshaller<String> unmarshaller;

    @BeforeClass
    public static void beforeClass() {
        marshaller = StringMarshaller.getInstance();
        unmarshaller = StringUnmarshaller.getInstance();
    }

    @Test
    public void testMarshalAndUnmarshal() throws Exception {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        final String source = "rkeat9awtk22";

        marshaller.marshal(packer, source);

        final String dest = unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray()));

        Assert.assertEquals(source, dest);
    }

    @Test
    public void testNullBMarshalAndUnmarshal() throws Exception {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        marshaller.marshal(packer, null);
        Assert.assertNull(unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray())));
    }
}
