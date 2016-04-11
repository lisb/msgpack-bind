package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.CollectionUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.StringUnmarshaller;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionMarshallerTest {

    private static Marshaller<Collection<String>> marshaller;
    private static Unmarshaller<? extends Collection<String>> unmarshaller;

    @BeforeClass
    public static void beforeClass() {
        marshaller = new CollectionMarshaller<String>(StringMarshaller.getInstance());
        unmarshaller = new CollectionUnmarshaller.ArrayListUnmarshaller<String>(StringUnmarshaller.getInstance());
    }

    @Test
    public void testMarshalAndUnmarshal() throws Throwable {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        final List<String> source = new ArrayList<String>();
        source.add("TEST1");
        source.add(null);

        marshaller.marshal(packer, source);

        final Collection<String> dest = unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray()));

        Assert.assertArrayEquals(source.toArray(new String[source.size()]), dest.toArray(new String[dest.size()]));
    }

    @Test
    public void testNullMarshalAndUnmarshal() throws Throwable {
        final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        marshaller.marshal(packer, null);

        Assert.assertNull(unmarshaller.unmarshal(MessagePack.newDefaultUnpacker(packer.toByteArray())));
    }
}
