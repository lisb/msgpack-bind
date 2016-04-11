#Usage Example

MessagePack-bind

##Primisives Examples

```Java
@MsgpackBind
public class User {
    @Property("name")
    public String displayName;
    public int age;
    @Ignore
    public boolean hidden;
    // private field is ignored.
    private boolean state;
}
```
// marshal
new UserMarshaller().marshal(packer, user);

// unmarshal
User user = new UserUnmarshaller().unmarshal(unpacker);
```

