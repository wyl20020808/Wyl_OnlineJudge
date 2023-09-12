package com.wyl.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * 虽然ObjectMapper已经足够好用，但是我还是不习惯，于是构建这个类来替代ObjectMapper的用法
 */
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    //    将对象转为Map
    public static Map<String, Object> convertObjectToMap(Object object) {
        return objectMapper.convertValue(object, Map.class);
    }

    //    将对象转为json字串
    public static String ObjectToJsonString(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    //    json字串转对象
    public static <T> T jsonToObject(String json, Class<T> targetClass) throws Exception {
        return objectMapper.readValue(json, targetClass);
    }
}
