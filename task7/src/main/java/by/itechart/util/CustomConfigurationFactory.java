package by.itechart.util;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MyXmlConfigurationFactory", category = "ConfigurationFactory")
@Order(50)
public class CustomConfigurationFactory extends ConfigurationFactory {

    /**
     * Valid file extensions for XML files.
     */
    public static final String[] SUFFIXES = new String[]{".xml", "*"};

    /**
     * Return the Configuration.
     *
     * @param source The InputSource.
     * @return The Configuration.
     */
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
        return new CustomConfiguration(loggerContext, source);
    }

    /**
     * Returns the file suffixes for XML files.
     *
     * @return An array of File extensions.
     */
    public String[] getSupportedTypes() {
        return SUFFIXES;
    }
}