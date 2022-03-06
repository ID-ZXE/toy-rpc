package com.github.netty.serialize;

import com.github.netty.handler.impl.JacksonSendHandler;
import com.github.netty.handler.NettyRpcSendHandler;
import com.github.serialize.RpcSerializeFrame;
import com.github.serialize.SerializeProtocol;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import io.netty.channel.ChannelPipeline;


public class RpcSendSerializeFrame implements RpcSerializeFrame {

    private static final ClassToInstanceMap<NettyRpcSendHandler> HANDLER = MutableClassToInstanceMap.create();

    static {
        HANDLER.putInstance(JacksonSendHandler.class, new JacksonSendHandler());
    }

    @Override
    public void select(SerializeProtocol protocol, ChannelPipeline pipeline) {
        if (protocol == SerializeProtocol.JACKSON) {
            HANDLER.getInstance(JacksonSendHandler.class).handle(pipeline);
        }
        throw new RuntimeException();
    }

}

