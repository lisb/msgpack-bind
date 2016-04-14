import com.lisb.msgpack.bind.Access;
import com.lisb.msgpack.bind.MsgpackBind;

@Access(Access.AccessType.PROPERTY)
@MsgpackBind
public class SubclassPropertySource extends AccessTypePropertySource {

    private String subFirst;
    private long subSecond;

    @Override
    public String getFirst() {
        return subFirst;
    }

    @Override
    public void setFirst(String first) {
        subFirst = first;
    }

    public long getSubSecond() {
        return subSecond;
    }

    public void setSubSecond(long subSecond) {
        this.subSecond = subSecond;
    }
}