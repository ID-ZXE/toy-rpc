package com.github.serialize.jackson;

import com.github.entity.MessageRequest;
import com.github.entity.MessageResponse;
import com.github.serialize.RpcSerialize;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;


public class JacksonSerialize implements RpcSerialize {

    private boolean rpcDirect = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public boolean isRpcDirect() {
        return rpcDirect;
    }

    public void setRpcDirect(boolean rpcDirect) {
        this.rpcDirect = rpcDirect;
    }

    @Override
    public Object deserialize(InputStream input) {
        try {
            String content = IOUtils.toString(input, StandardCharsets.UTF_8);
            Class<?> cls = isRpcDirect() ? MessageRequest.class : MessageResponse.class;
            return JacksonUtils.deserialize(content, cls);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void serialize(OutputStream output, Object object) {
        try {
            String content = JacksonUtils.serialize(object);
            LOGGER.info("serialize result:{}", content);
            IOUtils.write(content, output, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}

