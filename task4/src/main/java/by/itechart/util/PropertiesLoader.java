package by.itechart.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            properties.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }

    public static Properties loadProperties(){
        return loadProperties("authentication.properties");
    }
}
