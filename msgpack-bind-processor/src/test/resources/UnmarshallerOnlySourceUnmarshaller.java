import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.LongUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.ObjectUnmarshaller;
import java.io.IOException;
import java.lang.Long;
import java.lang.String;
import org.msgpack.core.MessageUnpacker;

public class UnmarshallerOnlySourceUnmarshaller extends ObjectUnmarshaller<UnmarshallerOnlySource> {
    private final Unmarshaller<Long> firstUnmarshaller = LongUnmarshaller.getInstance();

    protected boolean unmarshalProperty(MessageUnpacker unpacker, UnmarshallerOnlySource target, String propertyName) throws IOException {
        switch(propertyName) {
            case "first": {
                target.first = firstUnmarshaller.unmarshal(unpacker);
                return true;
            }
        }
        return false;
    }

    protected UnmarshallerOnlySource newInstance() {
        return new UnmarshallerOnlySource();
    }
}