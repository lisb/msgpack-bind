import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.marshaller.ObjectMarshaller;
import com.lisb.msgpack.bind.marshaller.StringMarshaller;
import java.io.IOException;
import java.lang.String;
import org.msgpack.core.MessagePacker;

public class AccessTypePropertySourceMarshaller extends ObjectMarshaller<AccessTypePropertySource> {

    private final Marshaller<String> firstMarshaller = StringMarshaller.getInstance();

    protected int getSize(AccessTypePropertySource target) {
        int mapSize = 0;
        if (target.getFirst() != null) {
            mapSize++;
        }
        return mapSize;
    }

    protected void marshalContent(MessagePacker packer, AccessTypePropertySource target) throws IOException {
        if (target.getFirst() != null) {
            packer.packString("first");
            firstMarshaller.marshal(packer, target.getFirst());
        }
    }
}