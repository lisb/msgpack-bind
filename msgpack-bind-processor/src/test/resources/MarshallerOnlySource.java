import com.lisb.msgpack.bind.GenerateType;
import com.lisb.msgpack.bind.MsgpackBind;

@MsgpackBind(GenerateType.MARSHALLER)
public class MarshallerOnlySource {
    public Long first;
}