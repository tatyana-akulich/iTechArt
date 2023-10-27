package test;

import com.microsoft.playwright.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import util.PropertiesLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

import static util.PropertiesLoader.*;

public class BaseTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page mainPage;
    public static Logger log;

    @BeforeAll
    void beforeAll() {
        PropertiesLoader.loadProperties();
        configureLogging();
        launchBrowser();
    }

    @AfterAll
    void afterAll() {
        closePlaywright();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions().setLocale("en-GB"));
        mainPage = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    void launchBrowser() {
        playwright = Playwright.create();
        BrowserType browserType = null;
        switch (getBrowserName()) {
            case "chrome": {
                browserType = playwright.chromium();
                break;
            }
            case "webkit": {
                browserType = playwright.webkit();
                break;
            }
            case "firefox": {
                browserType = playwright.firefox();
                break;
            }
            default: {
                log.error("Wrong browser name");
                System.exit(0);
            }
        }
        browser = browserType.launch(new BrowserType.LaunchOptions()
                .setHeadless(isHeadless()).setSlowMo(50));
    }

    void closePlaywright() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    public void configureLogging() {
        boolean logFileEnable = isLogFileEnable();
        String logFileName = getLogFileName();
        String logLevel = getLogLevel();

        final LoggerContext context = (LoggerContext) LogManager.getContext(false);
        final Configuration config = context.getConfiguration();
        final Layout layout = PatternLayout.createLayout("%d [%-5level] %m %n",
                null, config, null, null, false, false, null, null);

        final Appender consoleAppender = ConsoleAppender.createDefaultAppenderForLayout(layout);
        consoleAppender.start();
        config.addAppender(consoleAppender);
        AppenderRef[] refsForStdOut = new AppenderRef[]{AppenderRef.createAppenderRef(consoleAppender.getName(), Level.getLevel(logLevel), null)};
        LoggerConfig loggerConfigConsole = LoggerConfig.createLogger(false, Level.getLevel(logLevel), "org.apache.logging.log4j",
                "true", refsForStdOut, null, config, null);
        loggerConfigConsole.addAppender(consoleAppender, Level.getLevel(logLevel), null);
        config.addLogger("Console", loggerConfigConsole);

        FileAppender fileAppender = null;
        if (logFileEnable) {
            if (isLogFilePreserve()) {
                logFileName = logFileName + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            }
            fileAppender = FileAppender.createAppender(getLogDir() + "/" + logFileName + ".log", "false", "false",
                    "File", "false", "false", "false", "4000", layout, null, "false", null, config);
            fileAppender.start();
            config.addAppender(fileAppender);
            AppenderRef[] refsForFile = new AppenderRef[]{AppenderRef.createAppenderRef(fileAppender.getName(), Level.getLevel(logLevel), null)};
            LoggerConfig loggerConfigFile = LoggerConfig.createLogger(false, Level.getLevel(logLevel), "org.apache.logging.log4j",
                    "true", refsForFile, null, config, null);
            loggerConfigFile.addAppender(fileAppender, Level.getLevel(logLevel), null);
            config.addLogger("File", loggerConfigFile);
        }

        LoggerConfig rootConfig = config.getRootLogger();
        rootConfig.addAppender(consoleAppender, Level.getLevel(logLevel), null);
        if (logFileEnable) {
            rootConfig.addAppender(fileAppender, Level.getLevel(logLevel), null);
        }
        log = LogManager.getLogger(BaseTest.class);
    }

    public static Logger getLog() {
        return log;
    }
}
