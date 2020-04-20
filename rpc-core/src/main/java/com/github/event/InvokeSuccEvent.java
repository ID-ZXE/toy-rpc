package com.github.event;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import java.util.concurrent.atomic.AtomicLong;


public class InvokeSuccEvent extends AbstractInvokeEventBus {
    private AtomicLong sequenceInvokeSuccNumber = new AtomicLong(0L);

    public InvokeSuccEvent() {
        super();
    }

    public InvokeSuccEvent(String moduleName, String methodName) {
        super(moduleName, methodName);
    }

    @Override
    public Notification buildNotification(Object oldValue, Object newValue) {
        return new AttributeChangeNotification(this, sequenceInvokeSuccNumber.incrementAndGet(), System.currentTimeMillis(),
                super.moduleName, super.methodName, ModuleEvent.INVOKE_SUCC_EVENT.toString(), oldValue, newValue);
    }
}

