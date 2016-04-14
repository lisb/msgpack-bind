# msgpack-bind

## Primisives Examples


```Java
@MsgpackBind
public class User {
    // [implementing] @Property("name")
    public String displayName;
    public int age;
    // [implementing] @Ignore
    public boolean hidden;
    // private field is ignored.
    private boolean state;
}

// marshal
new UserMarshaller().marshal(packer, user);

// unmarshal
User user = new UserUnmarshaller().unmarshal(unpacker);
```

## Generate either Marshaller or Unmarshaller

Use `@MsgpackBind(GenerateType.MARSHALLER)` or `@MsgpackBind(GenerateType.UNMARSHALLER)`.

## Property-based access

Use `@Access(AccessType.PROPERTY)`.

## License

msgpack-bind is released under The MIT License.

```
Copyright (c) 2016 L is B Corp.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
