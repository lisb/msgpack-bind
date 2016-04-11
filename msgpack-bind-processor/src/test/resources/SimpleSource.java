import com.lisb.msgpack.bind.MsgpackBind;

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
    public byte[] seventh;

    public enum MimeTypes {
        IMAGE, VIDEO, SOUND
    }
}