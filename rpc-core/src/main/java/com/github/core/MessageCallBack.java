package com.github.core;

import com.github.exception.InvokeModuleException;
import com.github.exception.InvokeTimeoutException;
import com.github.exception.RejectResponeException;
import com.github.entity.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MessageCallBack {

    private MessageResponse response;

    private final Lock lock = new ReentrantLock();

    private final Condition finish = lock.newCondition();

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Object start() {
        lock.lock();
        try {
            // 等待服务端响应
            await();
            if (response != null) {
                boolean isInvokeSucc = getInvokeResult();
                if (isInvokeSucc) {
                    if (response.getError().isEmpty()) {
                        return response.getResult();
                    } else {
                        throw new InvokeModuleException(this.response.getError());
                    }
                } else {
                    throw new RejectResponeException(SystemConfig.FILTER_RESPONSE_MSG);
                }
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    public void over(MessageResponse response) {
        lock.lock();
        try {
            // 唤醒等待的线程
            finish.signal();
            this.response = response;
        } finally {
            lock.unlock();
        }
    }

    private void await() {
        boolean timeout = false;
        try {
            // 当前线程等待,默认3s
            // 被唤醒说明有返回结果
            timeout = finish.await(SystemConfig.SYSTEM_PROPERTY_MESSAGE_CALLBACK_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("MessageCallBack await error", e);
        }

        // 超时则抛出异常
        if (!timeout) {
            throw new InvokeTimeoutException(SystemConfig.TIMEOUT_RESPONSE_MSG);
        }
    }

    private boolean getInvokeResult() {
        return (!Objects.equals(this.response.getError(), SystemConfig.FILTER_RESPONSE_MSG) &&
                (!this.response.isReturnNotNull() || (this.response.isReturnNotNull() && this.response.getResult() != null)));
    }

}
