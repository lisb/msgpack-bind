import com.lisb.msgpack.bind.Access;
import com.lisb.msgpack.bind.MsgpackBind;

@Access(Access.AccessType.PROPERTY)
@MsgpackBind
public class ClassDifferentBetweenSetterAndGetterSource {

    public String getFirst() {
        return null;
    }

    public void setFirst(int first) {
    }

}