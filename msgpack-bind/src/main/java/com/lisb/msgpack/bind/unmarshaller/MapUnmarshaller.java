package com.lisb.msgpack.bind.unmarshaller;

import com.lisb.msgpack.bind.Unmarshaller;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapUnmarshaller<K, V> implements Unmarshaller<Map<K, V>> {

    private final Unmarshaller<K> keyUnmarshaller;
    private final Unmarshaller<V> valueUnmarshaller;

    public MapUnmarshaller(Unmarshaller<K> keyUnmarshaller, Unmarshaller<V> valueUnmarshaller) {
        this.keyUnmarshaller = keyUnmarshaller;
        this.valueUnmarshaller = valueUnmarshaller;
    }

    @Override
    public Map<K, V> unmarshal(MessageUnpacker unpacker) throws IOException {
        if (!unpacker.getNextFormat().getValueType().isNilType()) {
            final int size = unpacker.unpackMapHeader();
            final Map<K, V> map = new HashMap<K, V>(size * 2);
            for (int i = 0; i < size; i++) {
                map.put(keyUnmarshaller.unmarshal(unpacker), valueUnmarshaller.unmarshal(unpacker));
            }
            return map;
        } else {
            return null;
        }
    }
}
