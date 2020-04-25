
package com.github.event;

import com.github.jmx.ModuleMetricsHandler;

import java.util.EnumMap;
import java.util.Map;


public class InvokeEventBusFacade {

    private static final Map<AbstractInvokeEventBus.ModuleEvent, AbstractInvokeEventBus> enumMap = new EnumMap<>(AbstractInvokeEventBus.ModuleEvent.class);

    static {
        enumMap.put(AbstractInvokeEventBus.ModuleEvent.INVOKE_EVENT, new InvokeEvent());
        enumMap.put(AbstractInvokeEventBus.ModuleEvent.INVOKE_SUCC_EVENT, new InvokeSuccEvent());
        enumMap.put(AbstractInvokeEventBus.ModuleEvent.INVOKE_FAIL_EVENT, new InvokeFailEvent());
        enumMap.put(AbstractInvokeEventBus.ModuleEvent.INVOKE_FILTER_EVENT, new InvokeFilterEvent());
        enumMap.put(AbstractInvokeEventBus.ModuleEvent.INVOKE_TIMESPAN_EVENT, new InvokeTimeSpanEvent());
        enumMap.put(AbstractInvokeEventBus.ModuleEvent.INVOKE_MAX_TIMESPAN_EVENT, new InvokeMaxTimeSpanEvent());
        enumMap.put(AbstractInvokeEventBus.ModuleEvent.INVOKE_MIN_TIMESPAN_EVENT, new InvokeMinTimeSpanEvent());
        enumMap.put(AbstractInvokeEventBus.ModuleEvent.INVOKE_FAIL_STACKTRACE_EVENT, new InvokeFailStackTraceEvent());
    }

    public InvokeEventBusFacade(ModuleMetricsHandler handler, String moduleName, String methodName) {
        for (AbstractInvokeEventBus event : enumMap.values()) {
            event.setHandler(handler);
            event.setModuleName(moduleName);
            event.setMethodName(methodName);
        }
    }

    public AbstractInvokeEventBus fetchEvent(AbstractInvokeEventBus.ModuleEvent event) {
        return enumMap.getOrDefault(event, null);
    }
}

