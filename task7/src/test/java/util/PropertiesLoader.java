package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    static String browserName;
    static boolean headless;
    static String folderForDownloadFiles;
    static String logLevel;
    static String logDir;
    static String logFileName;
    static boolean logFileEnable;
    static boolean logFilePreserve;

    public static String getBrowserName() {
        return browserName;
    }

    public static boolean isHeadless() {
        return headless;
    }

    public static String getFolderForDownloadFiles() {
        return folderForDownloadFiles;
    }

    public static String getLogLevel() {
        return logLevel;
    }

    public static String getLogDir() {
        return logDir;
    }

    public static String getLogFileName() {
        return logFileName;
    }

    public static boolean isLogFileEnable() {
        return logFileEnable;
    }

    public static boolean isLogFilePreserve() {
        return logFilePreserve;
    }

    public static void loadProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            properties.load(inputStream);
            browserName = properties.getProperty("browser.name");
            headless = Boolean.parseBoolean(properties.getProperty("browser.headless"));
            folderForDownloadFiles = properties.getProperty("download.dir");
            logLevel = properties.getProperty("log.level");
            logDir = properties.getProperty("log.dir") == null ? "logs" : properties.getProperty("log.dir");
            logFileName = properties.getProperty("log.file.name") == null ? "test.DiscountTest" : properties.getProperty("log.file.name");
            logFileEnable = Boolean.parseBoolean(properties.getProperty("log.file.enabled"));
            logFilePreserve = Boolean.parseBoolean(properties.getProperty("log.file.preserve"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void loadProperties() {
        loadProperties("configuration.properties");
    }
}
