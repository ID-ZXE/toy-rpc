package com.github.serialize.jackson;

import com.github.entity.MessageRequest;
import com.github.entity.MessageResponse;
import com.github.serialize.MessageCodecUtil;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;


public class JacksonCodecUtil implements MessageCodecUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private boolean rpcDirect = false;

    public void setRpcDirect(boolean rpcDirect) {
        this.rpcDirect = rpcDirect;
    }

    @Override
    public void encode(final ByteBuf out, final Object message) {
        String serialize = JacksonUtils.serialize(message);
        LOGGER.debug("encode serialize:{}", serialize);
        byte[] body = serialize.getBytes(StandardCharsets.UTF_8);
        int dataLength = body.length;
        out.writeInt(dataLength);
        out.writeBytes(body);
    }

    @Override
    public Object decode(byte[] body) {
        Class<?> cls = rpcDirect ? MessageRequest.class : MessageResponse.class;
        return JacksonUtils.deserialize(new String(body), cls);
    }

}

