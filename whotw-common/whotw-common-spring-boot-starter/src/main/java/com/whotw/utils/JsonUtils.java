package com.whotw.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whotw.common.jackson.WhotwJavaTimeModule;

import java.io.IOException;
import java.util.Locale;

/**
 * @author EdisonXu
 * @date 2019-11-21
 */
public class JsonUtils {

    private static ThreadLocal<ObjectMapper>  mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    public static String writeJsonString(Object o) throws JsonProcessingException {
        return getObjectMapper().writeValueAsString(o);
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = mapperThreadLocal.get();
        //转换时忽略空值
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new WhotwJavaTimeModule());
        objectMapper.setLocale(Locale.CHINA);
        return objectMapper;
    }

    public static <T> T readValue(String content, Class<T> valueType) throws IOException {
        return getObjectMapper().readValue(content, valueType);
    }

    public static <T> T readValue(JsonParser jsonParser, Class<T> valueType) throws IOException {
        return getObjectMapper().readValue(jsonParser, valueType);
    }
}
