import com.lisb.msgpack.bind.MsgpackBind;
import com.lisb.msgpack.bind.Name;

import java.util.List;
import java.util.Map;

@MsgpackBind
public class SimpleSource {
    public String first;
    public int second;
    public List<String> third;
    public List<List<Boolean>> fourth;
    public Map<Long, String> fifth;
    public MimeTypes sixth;
    @Name("seven")
    public byte[] seventh;
    // private field is ignored.
    private long eighth;
    // static field is ignored.
    static Long nineth;
    // final field is ignored.
    final Short tenth = 1;

    public enum MimeTypes {
        IMAGE, VIDEO, SOUND
    }
}