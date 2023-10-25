import by.itechart.util.CustomConfigurationFactory;
import com.microsoft.playwright.*;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class BaseTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page mainPage;
    static Properties properties;
    static String browserName;
    static boolean headless;
    static String folderForDownloadFiles;
    public static String logLevel;
    public static String logDir;
    public static String logFileName;
    public static boolean logFileEnable;
    public static boolean logFilePreserve;

    @BeforeAll
    void beforeAll() {
        configureLogging();
        log.debug("Load properties");
        loadProperties();
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
        switch (browserName) {
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
                .setHeadless(headless).setSlowMo(50));
    }

    void closePlaywright() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    public static void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = BaseTest.class.getClassLoader().getResourceAsStream("configuration.properties")) {
            properties.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        log.debug("Get browserType from properties - {}", properties.getProperty("browser.name"));
        browserName = properties.getProperty("browser.name");
        log.debug("Get browser with set headless from properties - {}", properties.getProperty("browser.headless"));
        headless = Boolean.parseBoolean(properties.getProperty("headless"));
        log.debug("Get folder for download files from properties - {}", properties.getProperty("download.dir"));
        folderForDownloadFiles = properties.getProperty("download.dir");
    }

    public void configureLogging() {
        ConfigurationFactory configurationFactory = new CustomConfigurationFactory();
        ConfigurationFactory.setConfigurationFactory(configurationFactory);


        /*ConfigurationBuilder<BuiltConfiguration> builder
                = ConfigurationBuilderFactory.newConfigurationBuilder();
        AppenderComponentBuilder console
                = builder.newAppender("stdout", "Console");
        builder.add(console);
        AppenderComponentBuilder file
                = builder.newAppender("log", "File");
        file.addAttribute("fileName", "target/logging.log");
        builder.add(file);
        LayoutComponentBuilder standard
                = builder.newLayout("PatternLayout");
        standard.addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable");

        console.add(standard);
        file.add(standard);
        RootLoggerComponentBuilder rootLogger
                = builder.newRootLogger(Level.ERROR);
        rootLogger.add(builder.newAppenderRef("stdout"));

        builder.add(rootLogger);

        Configurator.initialize(builder.build());*/
    }
}
