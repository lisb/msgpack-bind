import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.marshaller.IntegerMarshaller;
import com.lisb.msgpack.bind.marshaller.LongMarshaller;
import com.lisb.msgpack.bind.marshaller.ObjectMarshaller;
import com.lisb.msgpack.bind.marshaller.StringMarshaller;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import org.msgpack.core.MessagePacker;

public class SubclassPropertySourceMarshaller extends ObjectMarshaller<SubclassPropertySource> {

    private final Marshaller<String> firstMarshaller = StringMarshaller.getInstance();
    private final Marshaller<Integer> threeMarshaller = IntegerMarshaller.getInstance();
    private final Marshaller<Long> subSecondMarshaller = LongMarshaller.getInstance();

    protected int getSize(SubclassPropertySource target) {
        int mapSize = 2;
        if (target.getFirst() != null) {
            mapSize++;
        }
        return mapSize;
    }

    protected void marshalContent(MessagePacker packer, SubclassPropertySource target) throws IOException {
        if (target.getFirst() != null) {
            packer.packString("first");
            firstMarshaller.marshal(packer, target.getFirst());
        }
        packer.packString("third");
        threeMarshaller.marshal(packer, target.getThree());
        packer.packString("subSecond");
        subSecondMarshaller.marshal(packer, target.getSubSecond());
    }
}