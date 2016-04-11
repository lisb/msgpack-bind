package com.lisb.msgpack.bind.marshaller;

import com.lisb.msgpack.bind.Marshaller;
import org.msgpack.core.MessagePacker;

import java.io.IOException;
import java.util.Map;

public class MapMarshaller<K, V> extends AbstractMarshaller<Map<K, V>> {

    private final Marshaller<K> keyMarshaller;
    private final Marshaller<V> valueMarshaller;

    public MapMarshaller(Marshaller<K> keyMarshaller, Marshaller<V> valueMarshaller) {
        this.keyMarshaller = keyMarshaller;
        this.valueMarshaller = valueMarshaller;
    }

    @Override
    public void _marshal(MessagePacker packer, Map<K, V> target) throws IOException {
        packer.packMapHeader(target.size());
        for (final Map.Entry<K, V> entry : target.entrySet()) {
            keyMarshaller.marshal(packer, entry.getKey());
            valueMarshaller.marshal(packer, entry.getValue());
        }
    }
}
