package com.github.test.serialize;

import com.github.entity.MessageResponse;
import com.github.serialize.jackson.JacksonUtils;
import com.github.test.entity.Student;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author zhanghang
 * @date 2022/3/6 12:50 上午
 * *****************
 * function:
 */
public class JacksonTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    public void test() {
        String json = "{\"@class\":\"com.github.entity.MessageResponse\",\"messageId\":\"9695baee-1de1-4132-82dc-1d7f722f20c6\"," +
                "\"result\":{\"@class\":\"com.github.test.entity.Student\",\"id\":1,\"name\":\"808b0db1-3eef-46f7-9f14-2da6618b1649\"},\"returnNotNull\":true}";
        MessageResponse response = JacksonUtils.deserialize(json, MessageResponse.class);
        Student stu = (Student) response.getResult();
        LOGGER.info("result:{}", stu);

        response.setError("");
        LOGGER.info("result:{}", JacksonUtils.serialize(response));
    }

}
