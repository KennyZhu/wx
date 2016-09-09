package com.kennyzhu.wx.core.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: KennyZhu
 * Date: 16/6/27
 * Desc:
 */
public final class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtil() {
    }

    private static ObjectMapper getObjectMapper() {
        MAPPER.getDeserializationConfig().with(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return MAPPER;
    }

    /**
     * 生成Json
     *
     * @param object
     * @return
     * @throws Exception
     */
    public static String getJsonString(Object object) {
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (IOException e) {
            LOGGER.error("Get json string error.Cause:", e);
        }
        return null;
    }

    public static Object getBeanByJson(String json, Class z) {
        try {
            return getObjectMapper().readValue(json, z);
        } catch (Exception e) {
            LOGGER.error("Parse json string error.json is " + json + "+Cause:", e);
        }
        return null;
    }

    public static Map jsonToMap(String json) {
        try {
            return getObjectMapper().readValue(json, Map.class);
        } catch (Exception e) {
            LOGGER.error("Parse json string error.json is " + json + "+Cause:", e);
        }
        return new HashMap();
    }

}
