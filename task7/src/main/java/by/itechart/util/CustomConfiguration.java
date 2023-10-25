package by.itechart.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class CustomConfiguration extends XmlConfiguration {

    CustomConfiguration(LoggerContext context, ConfigurationSource configSource) {
        super(context, configSource);
    }

    @Override
    protected void doConfigure() {
        super.doConfigure();
        final LoggerContext context = (LoggerContext) LogManager.getContext(false);
        final Configuration config = context.getConfiguration();
        final Layout layout = PatternLayout.createLayout("%d [%-5level] %m %n",
                null, config, null, null, false, false, null, null);

        Properties properties = new Properties();
        try (InputStream inputStream = CustomConfiguration.class.getClassLoader().getResourceAsStream("configuration.properties")) {
            properties.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String logLevel = properties.getProperty("log.level");
        String logDir = properties.getProperty("log.dir") == null ? "logs" : properties.getProperty("log.dir");
        String logFileName = properties.getProperty("log.file.name") == null ? "DiscountTest" : properties.getProperty("log.file.name");
        boolean logFileEnable = Boolean.parseBoolean(properties.getProperty("log.file.enabled"));
        boolean logFilePreserve = Boolean.parseBoolean(properties.getProperty("log.file.preserve"));

        if (logFileEnable) {
            if (logFilePreserve) {
                logFileName = logFileName + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            }

            final Appender fileAppender = FileAppender.createAppender(logDir + "/" + logFileName + ".log", "false", "false",
                    "File", "false", "false", "false", "4000", layout, null, "false", null, config);
            fileAppender.start();
            addAppender(fileAppender);
            AppenderRef[] refs = new AppenderRef[]{AppenderRef.createAppenderRef(fileAppender.getName(), Level.getLevel(logLevel), null)};
            LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.getLevel(logLevel), "org.apache.logging.log4j",
                    "true", refs, null, config, null);
            loggerConfig.addAppender(fileAppender, Level.getLevel(logLevel), null);
            addLogger("org.apache.logging.log4j", loggerConfig);
        }
        final Appender consoleAppender = ConsoleAppender.createDefaultAppenderForLayout(layout);
        consoleAppender.start();
        addAppender(consoleAppender);
        AppenderRef[] refsForSTDOUT = new AppenderRef[]{AppenderRef.createAppenderRef(consoleAppender.getName(), Level.getLevel(logLevel), null)};
        LoggerConfig loggerConfigForSTDOOT = LoggerConfig.createLogger(false, Level.getLevel(logLevel), "org.apache.logging.log4j",
                "true", refsForSTDOUT, null, config, null);
        loggerConfigForSTDOOT.addAppender(consoleAppender, Level.getLevel(logLevel), null);
        addLogger("org.apache.logging.log4j", loggerConfigForSTDOOT);


    }
}