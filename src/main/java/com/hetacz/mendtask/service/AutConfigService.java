package com.hetacz.mendtask.service;

import com.google.inject.Singleton;
import com.hetacz.mendtask.constants.AUT;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
@Slf4j
public class AutConfigService {

    private static final ConcurrentMap<AUT, Properties> autPropertiesMap = new ConcurrentHashMap<>();

    public void loadProperties(AUT aut) {
        String propertiesFileName = aut.toString().toLowerCase() + ".properties";
        Properties properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (is != null) {
                properties.load(is);
                autPropertiesMap.put(aut, properties);
            } else {
                log.error("{}.properties not found in classpath!", aut);
            }
        } catch (IOException e) {
            log.error("Error loading properties file '{}' for AUT: {}", propertiesFileName, aut, e);
        }
    }

    public String getProperty(AUT aut, String key) {
        Properties properties = autPropertiesMap.get(aut);
        if (properties == null) {
            log.error("Error reading property '{}' for AUT '{}'. Properties not loaded.", key, aut);
            throw new IllegalStateException("Properties for AUT '" + aut + "' not loaded.");
        }
        return properties.getProperty(key);
    }

    public String getProperty(AUT aut, String key, String defaultValue) {
        Properties properties = autPropertiesMap.get(aut);
        if (properties == null) {
            log.error("Error reading property '{}' for AUT '{}'. Properties not loaded.", key, aut);
            throw new IllegalStateException("Properties for AUT '" + aut + "' not loaded.");
        }
        return properties.getProperty(key, defaultValue);
    }
}
