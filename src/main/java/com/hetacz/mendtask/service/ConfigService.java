package com.hetacz.mendtask.service;

import com.google.inject.Singleton;
import com.hetacz.mendtask.constants.AUT;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Singleton
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigService {

    Properties properties = new Properties();

    public ConfigService() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                properties.load(is);
            } else {
                log.warn("config.properties not found in classpath!");
            }
        } catch (IOException e) {
            log.error("Failed to load config.properties", e);
        }
    }

    public String getProperty(String key) {
        String systemProperty = System.getProperty(key);
        return systemProperty != null ? systemProperty : properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public AUT getAut() {
        return AUT.valueOf(getProperty("aut").toUpperCase());
    }
}
