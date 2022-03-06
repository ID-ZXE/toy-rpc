package com.github.netty.handler;

import io.netty.channel.ChannelPipeline;

import java.util.Map;

public interface NettyRpcRecvHandler {

    /**
     * netty接收请求
     *
     * @param handlerMap
     * @param pipeline   Netty pipline
     */
    void handle(Map<String, Object> handlerMap, ChannelPipeline pipeline);

}

