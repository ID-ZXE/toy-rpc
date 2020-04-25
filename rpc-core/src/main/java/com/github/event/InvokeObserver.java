package com.github.event;

import com.github.jmx.ModuleMetricsVisitor;

import java.util.Observable;


public class InvokeObserver extends AbstractInvokeObserver {

    public InvokeObserver(InvokeEventBusFacade facade, ModuleMetricsVisitor visitor) {
        super(facade, visitor);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == AbstractInvokeEventBus.ModuleEvent.INVOKE_EVENT) {
            super.getFacade().fetchEvent(AbstractInvokeEventBus.ModuleEvent.INVOKE_EVENT).notify(super.getVisitor().getInvokeCount(), super.getVisitor().incrementInvokeCount());
        }
    }
}

