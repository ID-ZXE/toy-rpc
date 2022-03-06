package com.github.netty.serialize;

import com.github.netty.handler.impl.JacksonRecvHandler;
import com.github.netty.handler.NettyRpcRecvHandler;
import com.github.serialize.RpcSerializeFrame;
import com.github.serialize.SerializeProtocol;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

public class RpcRecvSerializeFrame implements RpcSerializeFrame {

    private final Map<String, Object> handlerMap;

    public RpcRecvSerializeFrame(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    private static final ClassToInstanceMap<NettyRpcRecvHandler> HANDLER = MutableClassToInstanceMap.create();

    static {
        HANDLER.putInstance(JacksonRecvHandler.class, new JacksonRecvHandler());
    }

    @Override
    public void select(SerializeProtocol protocol, ChannelPipeline pipeline) {
        if (protocol == SerializeProtocol.JACKSON) {
            HANDLER.getInstance(JacksonRecvHandler.class).handle(handlerMap, pipeline);
        }
        throw new RuntimeException();
    }

}
