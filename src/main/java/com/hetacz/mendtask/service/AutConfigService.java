package com.hetacz.mendtask.service;

import com.hetacz.mendtask.constants.AUT;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@UtilityClass
public class AutConfigService {

    private final Map<AUT, Properties> AUT_PROPERTIES_MAP = new EnumMap<>(AUT.class);

    static {
        Arrays.stream(AUT.values()).forEach(
                aut -> loadProperties(aut)
        );
    }

    private void loadProperties(AUT aut) {
        String propertiesFileName = aut.toString().toLowerCase() + ".properties";
        Properties properties = new Properties();
        try (InputStream is = AutConfigService.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (is != null) {
                properties.load(is);
                AUT_PROPERTIES_MAP.put(aut, properties);
                System.out.printf("Properties loaded for AUT %s baseUrl: %s%n", aut, getProperty(aut, "base.url"));
            } else {
                log.error("{}.properties not found in classpath!", aut);
            }
        } catch (IOException e) {
            log.error("Error loading properties file '{}' for AUT: {}", propertiesFileName, aut, e);
        }
    }

    public String getProperty(AUT aut, String key) {
        Properties properties = AUT_PROPERTIES_MAP.get(aut);
        if (properties == null) {
            log.error("Error reading property '{}' for AUT '{}'. Properties not loaded.", key, aut);
            throw new IllegalStateException("Properties for AUT '" + aut + "' not loaded.");
        }
        return properties.getProperty(key);
    }

    public String getProperty(AUT aut, String key, String defaultValue) {
        Properties properties = AUT_PROPERTIES_MAP.get(aut);
        if (properties == null) {
            log.error("Error reading property '{}' for AUT '{}'. Properties not loaded.", key, aut);
            throw new IllegalStateException("Properties for AUT '" + aut + "' not loaded.");
        }
        return properties.getProperty(key, defaultValue);
    }
}
