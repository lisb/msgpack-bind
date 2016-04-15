import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.ArrayUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.BooleanUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.ByteUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.CollectionUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.EnumUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.IntegerUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.LongUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.MapUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.ObjectUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.StringUnmarshaller;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import java.util.Map;
import org.msgpack.core.MessageUnpacker;

public class SubclassSourceUnmarshaller extends ObjectUnmarshaller<SubclassSource> {

    private final Unmarshaller<String> firstUnmarshaller = StringUnmarshaller.getInstance();
    private final Unmarshaller<Integer> secondUnmarshaller = IntegerUnmarshaller.getInstance();
    private final Unmarshaller<List<String>> thirdUnmarshaller = new CollectionUnmarshaller.ArrayListUnmarshaller(StringUnmarshaller.getInstance());
    private final Unmarshaller<List<List<Boolean>>> fourthUnmarshaller = new CollectionUnmarshaller.ArrayListUnmarshaller(new CollectionUnmarshaller.ArrayListUnmarshaller(BooleanUnmarshaller.getInstance()));
    private final Unmarshaller<Map<Long, String>> fifthUnmarshaller = new MapUnmarshaller(LongUnmarshaller.getInstance(), StringUnmarshaller.getInstance());
    private final Unmarshaller<SimpleSource.MimeTypes> sixthUnmarshaller = new EnumUnmarshaller(SimpleSource.MimeTypes.class);
    private final Unmarshaller<byte[]> seventhUnmarshaller = new ArrayUnmarshaller(byte.class, ByteUnmarshaller.getInstance());
    private final Unmarshaller<Long> subFirstUnmarshaller = LongUnmarshaller.getInstance();

    protected boolean unmarshalProperty(MessageUnpacker unpacker, SubclassSource target, String propertyName) throws IOException {
        switch (propertyName) {
            case "first": {
                target.first = firstUnmarshaller.unmarshal(unpacker);
                return true;
            }
            case "second": {
                target.second = secondUnmarshaller.unmarshal(unpacker);
                return true;
            }
            case "third": {
                target.third = thirdUnmarshaller.unmarshal(unpacker);
                return true;
            }
            case "fourth": {
                target.fourth = fourthUnmarshaller.unmarshal(unpacker);
                return true;
            }
            case "fifth": {
                target.fifth = fifthUnmarshaller.unmarshal(unpacker);
                return true;
            }
            case "sixth": {
                target.sixth = sixthUnmarshaller.unmarshal(unpacker);
                return true;
            }
            case "seven": {
                target.seventh = seventhUnmarshaller.unmarshal(unpacker);
                return true;
            }
            case "subFirst": {
                target.subFirst = subFirstUnmarshaller.unmarshal(unpacker);
                return true;
            }
        }
        return false;
    }

    protected SubclassSource newInstance() {
        return new SubclassSource();
    }
}