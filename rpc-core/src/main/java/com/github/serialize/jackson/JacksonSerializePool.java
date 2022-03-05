package com.github.serialize.jackson;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import static com.github.core.SystemConfig.*;


public class JacksonSerializePool {

    private final GenericObjectPool<JacksonSerialize> pool;

    private static final JacksonSerializePool JACKSON_SERIALIZE_POOL = new JacksonSerializePool(SERIALIZE_POOL_MAX_TOTAL, SERIALIZE_POOL_MIN_IDLE, SERIALIZE_POOL_MAX_WAIT_MILLIS,
            SERIALIZE_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS);

    public static JacksonSerializePool getJacksonSerializePoolInstance() {
        return JACKSON_SERIALIZE_POOL;
    }

    public JacksonSerializePool(final int maxTotal, final int minIdle, final long maxWaitMillis, final long minEvictableIdleTimeMillis) {
        pool = new GenericObjectPool<>(new JacksonSerializeFactory());

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

        pool.setConfig(config);
    }

    public JacksonSerialize borrow() {
        try {
            return getPool().borrowObject();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void restore(final JacksonSerialize object) {
        getPool().returnObject(object);
    }

    public GenericObjectPool<JacksonSerialize> getPool() {
        return pool;
    }

}

