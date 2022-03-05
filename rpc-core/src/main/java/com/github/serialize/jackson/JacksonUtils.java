package com.github.serialize.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author zhanghang
 * @date 2022/3/5 11:02 下午
 * *****************
 * function:
 */
public final class JacksonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String CLASS_KEY = "@class";

    private static final String JSON_ERROR_FORMAT = "Json 解析异常,Json:{}";

    private static final String JSON_ERROR_MESSAGE = "Json解析异常";

    static {
        FilterProvider theFilter = new SimpleFilterProvider().addFilter("outFilter", SimpleBeanPropertyFilter.serializeAllExcept());
        MAPPER.activateDefaultTypingAsProperty(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, CLASS_KEY);
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 排除空值, Strings.length()返回0, Java数组length=0等场景
        // MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //当bean没有属性不报错
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 主要用于日志答应输出全部
        MAPPER.setFilterProvider(theFilter);
    }

    private JacksonUtils() {
    }

    public static String getJsonNodeContent(String json, String nodeName) {
        try {
            return MAPPER.readTree(json).get(nodeName).toString();
        } catch (Exception e) {
            LOGGER.error(JSON_ERROR_FORMAT, json);
            throw new RuntimeException(JSON_ERROR_MESSAGE, e);
        }
    }

    public static String serialize(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(JSON_ERROR_MESSAGE, e);
        }
    }

    public static <T> T deserialize(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            LOGGER.error(JSON_ERROR_FORMAT, json);
            throw new RuntimeException(JSON_ERROR_MESSAGE, e);
        }
    }

    public static <T> T deserialize(String json, TypeReference<T> typeRef) {
        try {
            return MAPPER.readValue(json, typeRef);
        } catch (Exception e) {
            LOGGER.error(JSON_ERROR_FORMAT, json);
            throw new RuntimeException(JSON_ERROR_MESSAGE, e);
        }
    }

}
