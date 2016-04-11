package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.FloatUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.IntegerUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.MapUnmarshaller;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import java.util.HashMap;
import java.util.Map;

public class MapMarshallerTest {

    private static Marshaller<Map<Integer, Float>> marshaller;
    private static Unmarshaller<Map<Integer, Float>> unmarshaller;

    @BeforeClass
    public static void beforeClass() {
        marshaller = new MapMarshaller<Integer, Float>(IntegerMarshaller.getInstance(), FloatMarshaller.getInstance());
        unmarshaller = new MapUnmarshaller<Integer, Float>(IntegerUnmarshaller.getInstance(), FloatUnmarshaller.getInstance());
    }

    @Test
    public void testMarshalAndUnmarshal() throws Throwable {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        final Map<Integer, Float> source = new HashMap<Integer, Float>();
        source.put(5435, 348.6f);
        source.put(987, 0.645f);

        marshaller.marshal(packer, source);

        final Map<Integer, Float>  dest = unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray()));

        Assert.assertEquals(source, dest);
    }

    @Test
    public void testNullMarshalAndUnmarshal() throws Throwable {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        marshaller.marshal(packer, null);

        Assert.assertNull(unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray())));
    }
}
