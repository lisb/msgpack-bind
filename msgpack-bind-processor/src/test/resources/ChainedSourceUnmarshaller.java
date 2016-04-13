import com.lisb.msgpack.bind.Unmarshaller;
import com.lisb.msgpack.bind.unmarshaller.CollectionUnmarshaller;
import com.lisb.msgpack.bind.unmarshaller.ObjectUnmarshaller;
import java.io.IOException;
import java.lang.String;
import java.util.List;
import org.msgpack.core.MessageUnpacker;

public class ChainedSourceUnmarshaller extends ObjectUnmarshaller<ChainedSource> {

    private final Unmarshaller<SimpleSource> firstUnmarshaller = new SimpleSourceUnmarshaller();
    private final Unmarshaller<List<SimpleSource>> secondUnmarshaller = new CollectionUnmarshaller.ArrayListUnmarshaller(new SimpleSourceUnmarshaller());

    protected boolean unmarshalProperty(MessageUnpacker unpacker, ChainedSource target, String propertyName) throws IOException {
        switch (propertyName) {
            case "first": {
                target.first = firstUnmarshaller.unmarshal(unpacker);
                return true;
            }
            case "second": {
                target.second = secondUnmarshaller.unmarshal(unpacker);
                return true;
            }
        }
        return false;
    }

    protected ChainedSource newInstance() {
        return new ChainedSource();
    }
}