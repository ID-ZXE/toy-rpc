package com.github.netty.server;

import com.github.core.SystemConfig;
import com.github.entity.MessageRequest;
import com.github.entity.MessageResponse;
import com.github.parallel.NamedThreadFactory;
import com.github.parallel.RpcThreadPool;
import com.github.serialize.SerializeProtocol;
import com.google.common.util.concurrent.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.nio.channels.spi.SelectorProvider;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

@Data
public class MessageRecvExecutor {

    private String serverAddress;

    /**
     * 默认的序列化方式
     */
    private SerializeProtocol serializeProtocol = SerializeProtocol.JACKSON;

    /**
     * 切割ip与port
     */
    private static final String DELIMITER = SystemConfig.DELIMITER;

    /**
     * 两倍处理器数量
     */
    private static final int PARALLEL = SystemConfig.SYSTEM_PROPERTY_PARALLEL * 2;

    private static final int THREAD_NUMS = SystemConfig.SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS;

    private static final int QUEUE_NUMS = SystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS;

    /**
     * guava的线程池,能够添加监听器
     * 用于处理请求
     */
    private static final ListeningExecutorService LISTENING_EXECUTOR_SERVICE =
            MoreExecutors.listeningDecorator((ThreadPoolExecutor) RpcThreadPool.getExecutor(THREAD_NUMS, QUEUE_NUMS));

    private Map<String, Object> handlerMap = new ConcurrentHashMap<>();

    private final ThreadFactory toyRpcThreadFactory = new NamedThreadFactory("Toy-ThreadFactory");

    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    private EventLoopGroup workerGroup = new NioEventLoopGroup(PARALLEL, toyRpcThreadFactory, SelectorProvider.provider());

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private MessageRecvExecutor() {
        handlerMap.clear();
        register();
    }

    /**
     * 单例
     */
    private static class MessageRecvExecutorHolder {
        static final MessageRecvExecutor INSTANCE = new MessageRecvExecutor();
    }

    public static MessageRecvExecutor getInstance() {
        return MessageRecvExecutorHolder.INSTANCE;
    }

    public static void submit(Callable<Boolean> task, final ChannelHandlerContext ctx, final MessageRequest request, final MessageResponse response) {
        ListenableFuture<Boolean> listenableFuture = LISTENING_EXECUTOR_SERVICE.submit(task);
        Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                // 将消息刷新到client
                ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        LOGGER.info("RPC Server Send message-id response:{}", request.getMessageId());
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("send response failure", throwable);
            }

        }, LISTENING_EXECUTOR_SERVICE);
    }

    /**
     * 启动Netty服务器
     */
    public void start() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    // nio双向通道
                    .channel(NioServerSocketChannel.class)
                    // 子处理器, 用户处理workerGroup
                    // 每一个channel由多个handler组成pipeline
                    .childHandler(new MessageRecvChannelInitializer(handlerMap).buildRpcSerializeProtocol(serializeProtocol))
                    // BACKLOG用于构造服务端套接字ServerSocket对象, 标识当服务器请求处理线程全满时, 用于临时存放已完成三次握手的请求的队列的最大长度.
                    // 如果未设置或所设置的值小于1，Java将使用默认值50。
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 是否启用心跳保活机制. 在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）
                    // 并且在两个小时左右上层没有任何数据传输的情况下,这套机制才会被激活。
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] ipAddr = serverAddress.split(MessageRecvExecutor.DELIMITER);

            if (ipAddr.length == SystemConfig.IPADDR_OPRT_ARRAY_LENGTH) {
                final String host = ipAddr[0];
                final int port = Integer.parseInt(ipAddr[1]);
                ChannelFuture future = bootstrap.bind(host, port).sync();
                // 启动监听
                future.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            LOGGER.info("RPC Server start success!");
                            LOGGER.info("ip:{} port:{} protocol:{}", host, port, serializeProtocol);
                        }
                    }
                });
            } else {
                throw new RuntimeException("配置错误,请检查");
            }
        } catch (InterruptedException e) {
            LOGGER.info("RPC Server start error!", e);
        }
    }

    /**
     * 关闭server
     */
    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    private void register() {
    }

}
