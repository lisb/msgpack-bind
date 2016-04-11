import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.marshaller.CollectionMarshaller;
import com.lisb.msgpack.bind.marshaller.ObjectMarshaller;
import java.io.IOException;
import java.util.List;
import org.msgpack.core.MessagePacker;

public class ChainedSourceMarshaller extends ObjectMarshaller<ChainedSource> {

    private final Marshaller<SimpleSource> firstMarshaller = new SimpleSourceMarshaller();
    private final Marshaller<List<SimpleSource>> secondMarshaller = new CollectionMarshaller(new SimpleSourceMarshaller());

    protected int getSize(ChainedSource target) {
        int mapSize = 0;
        if (target.first != null) {
            mapSize++;
        }
        if (target.second != null) {
            mapSize++;
        }
        return mapSize;
    }

    protected void marshalContent(MessagePacker packer, ChainedSource target) throws IOException {
        if (target.first != null) {
            packer.packString("first");
            firstMarshaller.marshal(packer, target.first);
        }
        if (target.second != null) {
            packer.packString("second");
            secondMarshaller.marshal(packer, target.second);
        }
    }
}