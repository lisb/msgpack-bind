import com.lisb.msgpack.bind.Marshaller;
import com.lisb.msgpack.bind.marshaller.ArrayMarshaller;
import com.lisb.msgpack.bind.marshaller.BooleanMarshaller;
import com.lisb.msgpack.bind.marshaller.ByteMarshaller;
import com.lisb.msgpack.bind.marshaller.CollectionMarshaller;
import com.lisb.msgpack.bind.marshaller.EnumMarshaller;
import com.lisb.msgpack.bind.marshaller.IntegerMarshaller;
import com.lisb.msgpack.bind.marshaller.LongMarshaller;
import com.lisb.msgpack.bind.marshaller.MapMarshaller;
import com.lisb.msgpack.bind.marshaller.ObjectMarshaller;
import com.lisb.msgpack.bind.marshaller.StringMarshaller;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import java.util.Map;
import org.msgpack.core.MessagePacker;

public class SimpleSourceMarshaller extends ObjectMarshaller<SimpleSource> {

    private final Marshaller<String> firstMarshaller = StringMarshaller.getInstance();

    private final Marshaller<Integer> secondMarshaller = IntegerMarshaller.getInstance();

    private final Marshaller<List<String>> thirdMarshaller = new CollectionMarshaller(StringMarshaller.getInstance());

    private final Marshaller<List<List<Boolean>>> fourthMarshaller = new CollectionMarshaller(new CollectionMarshaller(BooleanMarshaller.getInstance()));

    private final Marshaller<Map<Long, String>> fifthMarshaller = new MapMarshaller(LongMarshaller.getInstance(), StringMarshaller.getInstance());

    private final Marshaller<SimpleSource.MimeTypes> sixthMarshaller = new EnumMarshaller();

    private final Marshaller<byte[]> seventhMarshaller = new ArrayMarshaller(ByteMarshaller.getInstance());

    protected int getSize(SimpleSource target) {
        int mapSize = 1;
        if (target.first != null) {
            mapSize++;
        }
        if (target.third != null) {
            mapSize++;
        }
        if (target.fourth != null) {
            mapSize++;
        }
        if (target.fifth != null) {
            mapSize++;
        }
        if (target.sixth != null) {
            mapSize++;
        }
        if (target.seventh != null) {
            mapSize++;
        }
        return mapSize;
    }

    protected void marshalContent(MessagePacker packer, SimpleSource target) throws IOException {
        if (target.first != null) {
            packer.packString("first");
            firstMarshaller.marshal(packer, target.first);
        }
        packer.packString("second");
        secondMarshaller.marshal(packer, target.second);
        if (target.third != null) {
            packer.packString("third");
            thirdMarshaller.marshal(packer, target.third);
        }
        if (target.fourth != null) {
            packer.packString("fourth");
            fourthMarshaller.marshal(packer, target.fourth);
        }
        if (target.fifth != null) {
            packer.packString("fifth");
            fifthMarshaller.marshal(packer, target.fifth);
        }
        if (target.sixth != null) {
            packer.packString("sixth");
            sixthMarshaller.marshal(packer, target.sixth);
        }
        if (target.seventh != null) {
            packer.packString("seventh");
            seventhMarshaller.marshal(packer, target.seventh);
        }
    }
}