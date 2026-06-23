package com.davitest.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("No se encontro el archivo: " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar config.properties", e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new RuntimeException("Propiedad no encontrada: " + key);
        }
        return value.trim();
    }

    public static String getOrDefault(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue)).trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static String getBrowser()        { return get("browser"); }
    public static String getBaseUrl()        { return get("base.url"); }
    public static int getExplicitWait()      { return getInt("explicit.wait"); }
    public static int getPageLoadTimeout()   { return getInt("page.load.timeout"); }
    public static boolean isHeadless()       { return getBoolean("headless"); }
    public static String getEnvironment()    { return get("environment"); }
}
