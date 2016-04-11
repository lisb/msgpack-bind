package com.lisb.msgpack.bind.marshaller;

import org.msgpack.core.MessagePacker;

import java.io.IOException;
import java.math.BigInteger;

public class BigIntegerMarshaller extends AbstractMarshaller<BigInteger> {

    private static final BigIntegerMarshaller instance = new BigIntegerMarshaller();

    public static BigIntegerMarshaller getInstance() {
        return instance;
    }

    private BigIntegerMarshaller(){
    }

    @Override
    protected void _marshal(MessagePacker packer, BigInteger target) throws IOException {
        packer.packBigInteger(target);
    }
}
