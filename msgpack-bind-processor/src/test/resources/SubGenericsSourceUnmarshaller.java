import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.IntegerUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.ObjectUnmarshaller;
import java.io.IOException;
import java.lang.Integer;
import java.lang.String;
import org.msgpack.core.MessageUnpacker;

public class SubGenericsSourceUnmarshaller extends ObjectUnmarshaller<SubGenericsSource> {

    private final Unmarshaller<Integer> firstUnmarshaller = IntegerUnmarshaller.getInstance();

    protected boolean unmarshalProperty(MessageUnpacker unpacker, SubGenericsSource target, String propertyName) throws IOException {
        switch (propertyName) {
            case "first": {
                target.first = firstUnmarshaller.unmarshal(unpacker);
                return true;
            }
        }
        return false;
    }

    protected SubGenericsSource newInstance() {
        return new SubGenericsSource();
    }
}