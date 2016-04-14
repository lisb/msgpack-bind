import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.marshaller.LongMarshaller;
import com.lisb.msgpack.bind.marshaller.ObjectMarshaller;
import java.io.IOException;
import java.lang.Long;
import org.msgpack.core.MessagePacker;

public class MarshallerOnlySourceMarshaller extends ObjectMarshaller<MarshallerOnlySource> {

    private final Marshaller<Long> firstMarshaller = LongMarshaller.getInstance();

    protected int getSize(MarshallerOnlySource target) {
        int mapSize = 0;
        if (target.first != null) {
            mapSize++;
        }
        return mapSize;
    }

    protected void marshalContent(MessagePacker packer, MarshallerOnlySource target) throws IOException {
        if (target.first != null) {
            packer.packString("first");
            firstMarshaller.marshal(packer, target.first);
        }
    }
}