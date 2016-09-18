package com.kennyzhu.wx.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yanlongzhu on 16/9/18.
 */
public final class ConfigProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigProxy.class);
    private static final String CONF_FILE_NAME = "config.properties";
    private static ConcurrentHashMap cache = new ConcurrentHashMap();

    private static final ConfigProxy INSTANCE = new ConfigProxy();

    private ConfigProxy() {
        load();
    }

    private boolean load() {
        Properties properties = new Properties();
        try {
            LOGGER.info("Begin to load config.");
            properties.load(new FileInputStream(CONF_FILE_NAME));
            Set<String> keys = properties.stringPropertyNames();
            for (String key : keys) {
                cache.put(key, properties.getProperty(key));
            }
            LOGGER.info("End to load config.Size is " + cache.size());
        } catch (Exception e) {
            LOGGER.error("Load config properties error.Cause:", e);
            return false;
        }
        return true;
    }

    public ConcurrentHashMap getCache() {
        return cache;
    }

    public boolean reload() {
        if (load()) {
            return true;
        }
        return false;
    }

}
