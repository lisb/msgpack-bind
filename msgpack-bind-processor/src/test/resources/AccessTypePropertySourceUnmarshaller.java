import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.ObjectUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.StringUnmarshaller;
import java.io.IOException;
import java.lang.String;
import org.msgpack.core.MessageUnpacker;

public class AccessTypePropertySourceUnmarshaller extends ObjectUnmarshaller<AccessTypePropertySource> {

    private final Unmarshaller<String> firstUnmarshaller = StringUnmarshaller.getInstance();

    protected boolean unmarshalProperty(MessageUnpacker unpacker, AccessTypePropertySource target, String propertyName) throws IOException {
        switch (propertyName) {
            case "first": {
                target.setFirst(firstUnmarshaller.unmarshal(unpacker));
                return true;
            }
        }
        return false;
    }

    protected AccessTypePropertySource newInstance() {
        return new AccessTypePropertySource();
    }
}