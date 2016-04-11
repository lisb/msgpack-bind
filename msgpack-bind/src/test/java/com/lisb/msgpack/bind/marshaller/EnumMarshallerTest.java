package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.EnumUnmarshaller;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

public class EnumMarshallerTest {
    private static Marshaller<Type> marshaller;
    private static Unmarshaller<Type> unmarshaller;

    @BeforeClass
    public static void beforeClass() {
        marshaller = new EnumMarshaller<Type>();
        unmarshaller = new EnumUnmarshaller<Type>(Type.class);
    }

    @Test
    public void testMarshalAndUnmarshal() throws Exception {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        marshaller.marshal(packer, Type.STAGING);
        Assert.assertEquals(Type.STAGING, unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray())));
    }

    @Test
    public void testNullBMarshalAndUnmarshal() throws Exception {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        marshaller.marshal(packer, null);
        Assert.assertNull(unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray())));
    }


    public enum Type {
        TEST, STAGING, PRODUCTION
    }
}
