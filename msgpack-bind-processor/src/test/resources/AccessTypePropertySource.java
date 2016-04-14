import com.lisb.msgpack.bind.Access;
import com.lisb.msgpack.bind.MsgpackBind;

@MsgpackBind
@Access(Access.AccessType.PROPERTY)
public class AccessTypePropertySource {

    private String first;
    // field is ignored.
    public String second;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    // the function is not getter, Because getter has no argument.
    public String getSecond(String second) {
        return second;
    }

    // the function is not setter, setter return void.
    public String setSecond(String second) {
        return second;
    }
}