package com.github.serialize.protostuff;

import com.github.serialize.MessageCodecUtil;
import com.google.common.io.Closer;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ProtostuffCodecUtil implements MessageCodecUtil {

    private static final Closer CLOSER = Closer.create();

    private final ProtostuffSerializePool POOL = ProtostuffSerializePool.getProtostuffPoolInstance();

    private boolean rpcDirect = false;

    public void setRpcDirect(boolean rpcDirect) {
        this.rpcDirect = rpcDirect;
    }

    @Override
    public void encode(final ByteBuf out, final Object message) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            CLOSER.register(byteArrayOutputStream);
            ProtostuffSerialize protostuffSerialization = POOL.borrow();
            protostuffSerialization.serialize(byteArrayOutputStream, message);
            byte[] body = byteArrayOutputStream.toByteArray();
            int dataLength = body.length;
            out.writeInt(dataLength);
            out.writeBytes(body);
            POOL.restore(protostuffSerialization);
        } finally {
            CLOSER.close();
        }
    }

    @Override
    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            CLOSER.register(byteArrayInputStream);
            ProtostuffSerialize protostuffSerialization = POOL.borrow();
            protostuffSerialization.setRpcDirect(rpcDirect);
            Object obj = protostuffSerialization.deserialize(byteArrayInputStream);
            POOL.restore(protostuffSerialization);
            return obj;
        } finally {
            CLOSER.close();
        }
    }
}

