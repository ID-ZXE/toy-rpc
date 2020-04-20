package com.github.listener;

import com.github.core.Modular;
import com.github.core.ModuleInvoker;
import com.github.core.ModuleProvider;
import com.github.model.MessageRequest;

import java.util.Collections;
import java.util.List;


public class ModuleListenerChainWrapper implements Modular {
    private Modular modular;
    private List<ModuleListener> listeners;

    public ModuleListenerChainWrapper(Modular modular) {
        if (modular == null) {
            throw new IllegalArgumentException("module is null");
        }
        this.modular = modular;
    }

    @Override
    public <T> ModuleProvider<T> invoke(ModuleInvoker<T> invoker, MessageRequest request) {
        return new ModuleProviderWrapper(modular.invoke(invoker, request), Collections.unmodifiableList(listeners), request);
    }

    public List<ModuleListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<ModuleListener> listeners) {
        this.listeners = listeners;
    }
}
