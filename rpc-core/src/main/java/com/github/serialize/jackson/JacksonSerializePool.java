package com.github.serialize.jackson;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import static com.github.core.SystemConfig.*;


public class JacksonSerializePool {

    private final GenericObjectPool<JacksonSerialize> protostuffPool;

    private static volatile JacksonSerializePool poolFactory = null;

    public static JacksonSerializePool getJacksonSerializePoolInstance() {
        if (poolFactory == null) {
            synchronized (JacksonSerializePool.class) {
                if (poolFactory == null) {
                    poolFactory = new JacksonSerializePool(SERIALIZE_POOL_MAX_TOTAL, SERIALIZE_POOL_MIN_IDLE, SERIALIZE_POOL_MAX_WAIT_MILLIS,
                            SERIALIZE_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS);
                }
            }
        }
        return poolFactory;
    }

    public JacksonSerializePool(final int maxTotal, final int minIdle, final long maxWaitMillis, final long minEvictableIdleTimeMillis) {
        protostuffPool = new GenericObjectPool<>(new JacksonSerializeFactory());

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

        protostuffPool.setConfig(config);
    }

    public JacksonSerialize borrow() {
        try {
            return getJacksonSerializePool().borrowObject();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void restore(final JacksonSerialize object) {
        getJacksonSerializePool().returnObject(object);
    }

    public GenericObjectPool<JacksonSerialize> getJacksonSerializePool() {
        return protostuffPool;
    }

}

