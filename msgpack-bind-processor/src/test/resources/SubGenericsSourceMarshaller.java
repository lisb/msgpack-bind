import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.marshaller.IntegerMarshaller;
import com.lisb.msgpack.bind.marshaller.ObjectMarshaller;
import java.io.IOException;
import java.lang.Integer;
import org.msgpack.core.MessagePacker;

public class SubGenericsSourceMarshaller extends ObjectMarshaller<SubGenericsSource> {

    private final Marshaller<Integer> firstMarshaller = IntegerMarshaller.getInstance();

    protected int getSize(SubGenericsSource target) {
        int mapSize = 0;
        if (target.first != null) {
            mapSize++;
        }
        return mapSize;
    }

    protected void marshalContent(MessagePacker packer, SubGenericsSource target) throws IOException {
        if (target.first != null) {
            packer.packString("first");
            firstMarshaller.marshal(packer, target.first);
        }
    }
}