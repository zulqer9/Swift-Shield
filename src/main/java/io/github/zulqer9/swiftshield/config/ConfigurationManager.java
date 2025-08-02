package io.github.zulqer9.swiftshield.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Configuration manager that handles application settings from various sources.
 * Provides a centralized way to access configuration properties with type safety.
 * 
 * @author zulqer9
 * @since 2.0.0
 */
public final class ConfigurationManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);
    private static final String PROPERTIES_FILE = "application.properties";
    private static final String JSON_CONFIG_FILE = "config.json";
    
    private static ConfigurationManager instance;
    private final Properties properties;
    private final Map<String, Object> jsonConfig;
    
    private ConfigurationManager() {
        this.properties = loadProperties();
        this.jsonConfig = loadJsonConfig();
    }
    
    /**
     * Gets the singleton instance of ConfigurationManager.
     * 
     * @return the configuration manager instance
     */
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }
    
    /**
     * Gets a string property value.
     * 
     * @param key the property key
     * @return the property value, or empty if not found
     */
    public Optional<String> getString(String key) {
        return Optional.ofNullable(properties.getProperty(key));
    }
    
    /**
     * Gets a string property value with a default.
     * 
     * @param key the property key
     * @param defaultValue the default value
     * @return the property value or default
     */
    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Gets an integer property value.
     * 
     * @param key the property key
     * @return the property value, or empty if not found or invalid
     */
    public Optional<Integer> getInt(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key '{}': {}", key, value, e);
            return Optional.empty();
        }
    }
    
    /**
     * Gets an integer property value with a default.
     * 
     * @param key the property key
     * @param defaultValue the default value
     * @return the property value or default
     */
    public int getInt(String key, int defaultValue) {
        return getInt(key).orElse(defaultValue);
    }
    
    /**
     * Gets a boolean property value.
     * 
     * @param key the property key
     * @return the property value, or empty if not found
     */
    public Optional<Boolean> getBoolean(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(Boolean.parseBoolean(value));
    }
    
    /**
     * Gets a boolean property value with a default.
     * 
     * @param key the property key
     * @param defaultValue the default value
     * @return the property value or default
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(key).orElse(defaultValue);
    }
    
    /**
     * Gets a value from the JSON configuration.
     * 
     * @param path the dot-separated path to the value
     * @param type the expected type
     * @param <T> the type parameter
     * @return the configuration value, or empty if not found
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getJsonValue(String path, Class<T> type) {
        try {
            String[] keys = path.split("\\.");
            Object current = jsonConfig;
            
            for (String key : keys) {
                if (current instanceof Map) {
                    current = ((Map<String, Object>) current).get(key);
                    if (current == null) {
                        return Optional.empty();
                    }
                } else {
                    return Optional.empty();
                }
            }
            
            if (type.isInstance(current)) {
                return Optional.of(type.cast(current));
            }
            
            return Optional.empty();
        } catch (Exception e) {
            logger.warn("Error getting JSON value for path '{}': {}", path, e.getMessage(), e);
            return Optional.empty();
        }
    }
    
    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (is != null) {
                props.load(is);
                logger.info("Loaded {} properties from {}", props.size(), PROPERTIES_FILE);
            } else {
                logger.warn("Properties file '{}' not found in classpath", PROPERTIES_FILE);
            }
        } catch (IOException e) {
            logger.error("Error loading properties from '{}'", PROPERTIES_FILE, e);
        }
        return props;
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, Object> loadJsonConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(JSON_CONFIG_FILE);
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            
            if (is == null) {
                logger.warn("JSON config file '{}' not found in classpath", JSON_CONFIG_FILE);
                return Map.of();
            }
            
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> config = gson.fromJson(reader, type);
            
            logger.info("Loaded JSON configuration from {}", JSON_CONFIG_FILE);
            return config != null ? config : Map.of();
            
        } catch (IOException e) {
            logger.error("Error loading JSON config from '{}'", JSON_CONFIG_FILE, e);
            return Map.of();
        }
    }
}