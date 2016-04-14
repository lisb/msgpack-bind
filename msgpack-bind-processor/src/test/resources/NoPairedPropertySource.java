import com.lisb.msgpack.bind.Access;
import com.lisb.msgpack.bind.MsgpackBind;

@Access(Access.AccessType.PROPERTY)
@MsgpackBind
public class NoPairedPropertySource {
    // has no paired setter
    public String getFirst() {
        return null;
    }
}