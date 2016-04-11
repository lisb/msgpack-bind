package com.lisb.msgpack.bind.unmarshaller;

import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.math.BigInteger;

public class BigIntegerUnmarshaller extends AbstractUnmarshaller<BigInteger> {

    private static final BigIntegerUnmarshaller instance = new BigIntegerUnmarshaller();

    public static BigIntegerUnmarshaller getInstance() {
        return instance;
    }

    private BigIntegerUnmarshaller() {
    }

    @Override
    public BigInteger _unmarshal(MessageUnpacker unpacker) throws IOException {
        return unpacker.unpackBigInteger();
    }
}
