package com.github.serialize.jackson;

import com.github.entity.MessageRequest;
import com.github.entity.MessageResponse;
import com.github.serialize.MessageCodecUtil;
import com.google.common.io.Closer;
import io.netty.buffer.ByteBuf;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;


public class JacksonCodecUtil implements MessageCodecUtil {

    private static final Closer CLOSER = Closer.create();

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private boolean rpcDirect = false;

    public void setRpcDirect(boolean rpcDirect) {
        this.rpcDirect = rpcDirect;
    }

    @Override
    public void encode(final ByteBuf out, final Object message) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            CLOSER.register(byteArrayOutputStream);
            serialize(byteArrayOutputStream, message);
            byte[] body = byteArrayOutputStream.toByteArray();
            int dataLength = body.length;
            out.writeInt(dataLength);
            out.writeBytes(body);
        } finally {
            CLOSER.close();
        }
    }

    @Override
    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            CLOSER.register(byteArrayInputStream);
            return deserialize(byteArrayInputStream);
        } finally {
            CLOSER.close();
        }
    }

    private Object deserialize(InputStream input) {
        try {
            String content = IOUtils.toString(input, StandardCharsets.UTF_8);
            Class<?> cls = rpcDirect ? MessageRequest.class : MessageResponse.class;
            return JacksonUtils.deserialize(content, cls);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void serialize(OutputStream output, Object object) {
        try {
            String content = JacksonUtils.serialize(object);
            LOGGER.info("serialize result:{}", content);
            IOUtils.write(content, output, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}

