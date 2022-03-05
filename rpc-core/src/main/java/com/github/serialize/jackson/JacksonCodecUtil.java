package com.github.serialize.jackson;

import com.github.serialize.MessageCodecUtil;
import com.google.common.io.Closer;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class JacksonCodecUtil implements MessageCodecUtil {

    private static final Closer CLOSER = Closer.create();

    private static final JacksonSerializePool POOL = JacksonSerializePool.getJacksonSerializePoolInstance();

    private boolean rpcDirect = false;

    public void setRpcDirect(boolean rpcDirect) {
        this.rpcDirect = rpcDirect;
    }

    @Override
    public void encode(final ByteBuf out, final Object message) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            CLOSER.register(byteArrayOutputStream);
            JacksonSerialize jacksonSerialize = POOL.borrow();
            jacksonSerialize.serialize(byteArrayOutputStream, message);
            byte[] body = byteArrayOutputStream.toByteArray();
            int dataLength = body.length;
            out.writeInt(dataLength);
            out.writeBytes(body);
            POOL.restore(jacksonSerialize);
        } finally {
            CLOSER.close();
        }
    }

    @Override
    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            CLOSER.register(byteArrayInputStream);
            JacksonSerialize jacksonSerialize = POOL.borrow();
            jacksonSerialize.setRpcDirect(rpcDirect);
            Object obj = jacksonSerialize.deserialize(byteArrayInputStream);
            POOL.restore(jacksonSerialize);
            return obj;
        } finally {
            CLOSER.close();
        }
    }
}

