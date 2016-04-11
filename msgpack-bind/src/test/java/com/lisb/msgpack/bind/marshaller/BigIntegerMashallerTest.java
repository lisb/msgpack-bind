package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.BigIntegerUnmarshaller;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import java.math.BigInteger;

public class BigIntegerMashallerTest {
    private static Marshaller<BigInteger> marshaller;
    private static Unmarshaller<BigInteger> unmarshaller;

    @BeforeClass
    public static void beforeClass() {
        marshaller = BigIntegerMarshaller.getInstance();
        unmarshaller = BigIntegerUnmarshaller.getInstance();
    }

    @Test
    public void testMarshalAndUnmarshal() throws Exception {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        final BigInteger source = new BigInteger(new byte[] { 127, -128, 15 });

        marshaller.marshal(packer, source);

        final BigInteger dest = unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray()));

        Assert.assertEquals(source, dest);
    }

    @Test
    public void testNullBMarshalAndUnmarshal() throws Exception {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        marshaller.marshal(packer, null);
        Assert.assertNull(unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray())));
    }
}
