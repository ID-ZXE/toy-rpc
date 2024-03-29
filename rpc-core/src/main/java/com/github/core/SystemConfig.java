package com.github.core;


public final class SystemConfig {

    private SystemConfig() {
    }

    public static final String SYSTEM_PROPERTY_THREADPOOL_REJECTED_POLICY_ATTR = "rpc.parallel.rejected.policy";

    public static final String SYSTEM_PROPERTY_THREADPOOL_QUEUE_NAME_ATTR = "rpc.parallel.queue";

    public static final long SYSTEM_PROPERTY_MESSAGE_CALLBACK_TIMEOUT = Long.getLong("rpc.default.msg.timeout", 30 * 1000L);

    public static final int SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS = Integer.getInteger("rpc.default.thread.nums", 16);

    public static final int SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS = Integer.getInteger("rpc.default.queue.nums", -1);

    public static final int SYSTEM_PROPERTY_CLIENT_RECONNECT_DELAY = Integer.parseInt(System.getProperty("rpc.default.client.reconnect.delay", "10"));

    /**
     * 处理器数量
     */
    public static final int SYSTEM_PROPERTY_PARALLEL = Math.max(2, Runtime.getRuntime().availableProcessors());

    public static final String DELIMITER = ":";

    public static final int IPADDR_OPRT_ARRAY_LENGTH = 2;

    public static final String FILTER_RESPONSE_MSG = "Illegal Request,RPC Server Refused To Respond!";

    public static final String TIMEOUT_RESPONSE_MSG = "Timeout Request,RPC Server Request Timeout!";

    public static final int SERIALIZE_POOL_MAX_TOTAL = 500;

    public static final int SERIALIZE_POOL_MIN_IDLE = 10;

    public static final int SERIALIZE_POOL_MAX_WAIT_MILLIS = 5000;

    public static final int SERIALIZE_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS = 600000;

}

