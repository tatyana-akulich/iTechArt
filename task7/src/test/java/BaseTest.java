import com.microsoft.playwright.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
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

        LoggerContext contextLogger = (LoggerContext) LogManager.getContext(false);
        Configuration config = contextLogger.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig("logger");
        loggerConfig.setLevel(Level.getLevel(logLevel));
        final Layout layout = PatternLayout.createLayout("%d [%-5level] %m %n",
                null, config, null, null, false, false, null, null);
        if (logFileEnable) {
            if (isLogFilePreserve()) {
                logFileName = logFileName + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            }
            FileAppender fileAppender = FileAppender.createAppender(getLogDir() + "/" + logFileName + ".log", "false", "false",
                    "File", "false", "false", "false", "4000", layout, null, "false", null, config);
            fileAppender.start();
            loggerConfig.addAppender(fileAppender, Level.getLevel(logLevel), null);
        }
        log = LogManager.getLogger();
    }

    public static Logger getLog() {
        return log;
    }
}
