import com.lisb.msgpack.bind.MsgpackBind;

@MsgpackBind(MsgpackBind.GenerateType.MARSHALLER)
public class MarshallerOnlySource {
    public Long first;
}