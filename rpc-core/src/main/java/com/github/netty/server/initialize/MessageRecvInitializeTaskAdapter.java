package com.github.netty.server.initialize;

import com.github.model.MessageRequest;
import com.github.model.MessageResponse;
import com.github.netty.server.initialize.AbstractMessageRecvInitializeTask;

import java.util.Map;

public class MessageRecvInitializeTaskAdapter extends AbstractMessageRecvInitializeTask {

    public MessageRecvInitializeTaskAdapter(MessageRequest request, MessageResponse response, Map<String, Object> handlerMap) {
        super(request, response, handlerMap);
    }

    @Override
    protected void injectInvoke() {

    }

    @Override
    protected void injectSuccInvoke(long invokeTimespan) {

    }

    @Override
    protected void injectFailInvoke(Throwable error) {

    }

    @Override
    protected void injectFilterInvoke() {

    }

    @Override
    protected void acquire() {

    }

    @Override
    protected void release() {

    }

}
