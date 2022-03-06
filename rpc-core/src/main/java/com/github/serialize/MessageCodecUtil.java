package com.github.serialize;

import io.netty.buffer.ByteBuf;

import java.io.IOException;


public interface MessageCodecUtil {

    int MESSAGE_LENGTH = 4;

    /**
     * 编码
     *
     * @param out
     * @param message
     * @throws IOException
     */
    void encode(final ByteBuf out, final Object message) throws IOException;

    /**
     * 解码
     *
     * @param body
     * @return
     * @throws IOException
     */
    Object decode(byte[] body) throws IOException;

}
