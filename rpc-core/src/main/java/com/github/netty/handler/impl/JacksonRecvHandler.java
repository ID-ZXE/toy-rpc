package com.github.netty.handler.impl;

import com.github.netty.handler.NettyRpcRecvHandler;
import com.github.netty.server.MessageRecvHandler;
import com.github.serialize.jackson.JacksonCodecUtil;
import com.github.serialize.jackson.JacksonDecoder;
import com.github.serialize.jackson.JacksonEncoder;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

public class JacksonRecvHandler implements NettyRpcRecvHandler {

    @Override
    public void handle(Map<String, Object> handlerMap, ChannelPipeline pipeline) {
        JacksonCodecUtil util = new JacksonCodecUtil();
        util.setRpcDirect(true);

        // 设置netty的处理链条,实现了netty的编/解码接口
        // MessageToByteEncoder
        // ByteToMessageDecoder
        pipeline.addLast(new JacksonEncoder(util));
        pipeline.addLast(new JacksonDecoder(util));
        // 设置入站处理器
        pipeline.addLast(new MessageRecvHandler(handlerMap));
    }

}

