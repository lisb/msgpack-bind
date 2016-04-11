import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.CollectionUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.IntegerUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.ObjectUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.StringUnmarshaller;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;

public class SimpleSourceUnmarshaller extends ObjectUnmarshaller<SimpleSource> {

    private final Unmarshaller<String> firstUnmarshaller = StringUnmarshaller.getInstance();
    private final Unmarshaller<Integer> secondUnmarshaller = IntegerUnmarshaller.getInstance();
    private final Unmarshaller<Integer> thirdUnmarshaller = new CollectionUnmarshaller.ArrayListUnmarshaller
    private final Unmarshaller<Integer> secondUnmarshaller = IntegerUnmarshaller.getInstance();
    private final Unmarshaller<Integer> secondUnmarshaller = IntegerUnmarshaller.getInstance();
    private final Unmarshaller<Integer> secondUnmarshaller = IntegerUnmarshaller.getInstance();

    @Override
    protected boolean unmarshalProperty(SimpleSource obj, String key, MessageUnpacker unpacker) throws IOException {
        switch (key) {
            case "first":
                obj.first = firstUnmarshaller.unmarshal(unpacker);
                return true;
            case "second":
                obj.seond = secondUnmarshaller.unmarshal(unpacker);
                return true;;
            case "third":
                obj.third =
                return true;;
            case "fourth":
                return true;;
            case "fifth":
                return true;;
            case "sixth":
                return true;;
            case "seventh":
                return true;;
        }
        return false;
    }

    @Override
    protected SimpleSource newInstance() {
        return new SimpleSource();
    }
}