package com.hetacz.mendtask.service;

import com.hetacz.mendtask.constants.AUT;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
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

    public String getPlatformProperty(String key) {
        return getProperty(getAUT() + "." + key);
    }

    public String getPlatformProperty(String key, String defaultValue) {
        return getProperty(getAUT() + "." + key, defaultValue);
    }

    public List<String> getReposToKeep() {
        return Arrays.stream(getProperty(getAUT() + "." + "keep").split(";")).toList();
    }

    private String getAUT() {
        String systemProperty = System.getProperty("aut");
        String stringProperty = systemProperty != null ? systemProperty : properties.getProperty("aut");
        return AUT.valueOf(stringProperty).toString().toLowerCase();
    }
}
