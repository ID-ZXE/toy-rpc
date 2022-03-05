package com.github.netty.handler.impl;

import com.github.netty.client.MessageSendHandler;
import com.github.netty.handler.NettyRpcSendHandler;
import com.github.serialize.jackson.JacksonCodecUtil;
import com.github.serialize.jackson.JacksonDecoder;
import com.github.serialize.jackson.JacksonEncoder;
import io.netty.channel.ChannelPipeline;

public class JacksonSendHandler implements NettyRpcSendHandler {

    @Override
    public void handle(ChannelPipeline pipeline) {
        JacksonCodecUtil util = new JacksonCodecUtil();
        util.setRpcDirect(false);
        pipeline.addLast(new JacksonEncoder(util));
        pipeline.addLast(new JacksonDecoder(util));
        pipeline.addLast(new MessageSendHandler());
    }

}
