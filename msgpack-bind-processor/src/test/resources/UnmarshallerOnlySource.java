import com.lisb.msgpack.bind.GenerateType;
import com.lisb.msgpack.bind.MsgpackBind;

@MsgpackBind(GenerateType.UNMARSHALLER)
public class UnmarshallerOnlySource {
    public Long first;
}