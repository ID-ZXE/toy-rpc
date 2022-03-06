package com.github.netty.client;

import com.github.core.MessageCallBack;
import com.github.entity.MessageRequest;
import com.github.entity.MessageResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class MessageSendHandler extends ChannelInboundHandlerAdapter {

    private final ConcurrentHashMap<String, MessageCallBack> callBackMap = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Channel channel;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        channel = ctx.channel();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageResponse response = (MessageResponse) msg;
        String messageId = response.getMessageId();
        // 根据messageId操作future
        MessageCallBack callBack = callBackMap.get(messageId);
        if (callBack != null) {
            callBackMap.remove(messageId);
            // 收到服务端响应
            callBack.over(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("MessageSendHandler.exceptionCaught error", cause);
        ctx.close();
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    public MessageCallBack sendRequest(MessageRequest request) {
        MessageCallBack callBack = new MessageCallBack();
        callBackMap.put(request.getMessageId(), callBack);
        channel.writeAndFlush(request);
        return callBack;
    }

}
