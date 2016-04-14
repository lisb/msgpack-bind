import com.lisb.msgpack.bind.MsgpackBind;

@MsgpackBind(MsgpackBind.GenerateType.UNMARSHALLER)
public class UnmarshallerOnlySource {
    public Long first;
}