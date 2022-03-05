package com.github.serialize;

import io.netty.channel.ChannelPipeline;

public interface RpcSerializeFrame {

    /**
     * 选择序列化方式
     *
     * @param protocol
     * @param pipeline
     */
    void select(SerializeProtocol protocol, ChannelPipeline pipeline);

}

