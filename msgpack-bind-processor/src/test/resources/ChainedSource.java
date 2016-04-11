import com.lisb.msgpack.bind.MsgpackBind;

import java.util.List;

@MsgpackBind
public class ChainedSource {
    public SimpleSource first;
    public List<SimpleSource> second;
}