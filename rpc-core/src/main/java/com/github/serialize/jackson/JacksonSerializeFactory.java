package com.github.serialize.jackson;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;


public class JacksonSerializeFactory extends BasePooledObjectFactory<JacksonSerialize> {

    @Override
    public JacksonSerialize create() throws Exception {
        return createJacksonSerialize();
    }

    @Override
    public PooledObject<JacksonSerialize> wrap(JacksonSerialize jacksonSerialize) {
        return new DefaultPooledObject<>(jacksonSerialize);
    }

    private JacksonSerialize createJacksonSerialize() {
        return new JacksonSerialize();
    }

}
