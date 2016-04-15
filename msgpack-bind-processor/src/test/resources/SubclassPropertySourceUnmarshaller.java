import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.IntegerUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.LongUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.ObjectUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.StringUnmarshaller;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import org.msgpack.core.MessageUnpacker;

public class SubclassPropertySourceUnmarshaller extends ObjectUnmarshaller<SubclassPropertySource> {

    private final Unmarshaller<String> firstUnmarshaller = StringUnmarshaller.getInstance();
    private final Unmarshaller<Integer> threeUnmarshaller = IntegerUnmarshaller.getInstance();
    private final Unmarshaller<Long> subSecondUnmarshaller = LongUnmarshaller.getInstance();

    protected boolean unmarshalProperty(MessageUnpacker unpacker, SubclassPropertySource target, String propertyName) throws IOException {
        switch (propertyName) {
            case "first": {
                target.setFirst(firstUnmarshaller.unmarshal(unpacker));
                return true;
            }
            case "third": {
                target.setThree(threeUnmarshaller.unmarshal(unpacker));
                return true;
            }
            case "subSecond": {
                target.setSubSecond(subSecondUnmarshaller.unmarshal(unpacker));
                return true;
            }
        }
        return false;
    }

    protected SubclassPropertySource newInstance() {
        return new SubclassPropertySource();
    }
}