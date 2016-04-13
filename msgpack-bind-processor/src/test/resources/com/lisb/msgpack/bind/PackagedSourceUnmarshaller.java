package com.lisb.msgpack.bind;

import com.lisb.msgpack.bind.unmarshaller.IntegerUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.ObjectUnmarshaller;
import java.io.IOException;
import java.lang.Integer;
import java.lang.String;
import org.msgpack.core.MessageUnpacker;

public class PackagedSourceUnmarshaller extends ObjectUnmarshaller<PackagedSource> {

    private final Unmarshaller<Integer> firstUnmarshaller = IntegerUnmarshaller.getInstance();

    protected boolean unmarshalProperty(MessageUnpacker unpacker, PackagedSource target, String propertyName) throws IOException {
        switch (propertyName) {
            case "first": {
                target.first = firstUnmarshaller.unmarshal(unpacker);
                return true;
            }
        }
        return false;
    }

    protected PackagedSource newInstance() {
        return new PackagedSource();
    }
}