import by.itechart.page.LoginPage;
import by.itechart.page.ProfilePage;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BaseTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    APIRequestContext request;
    String stateFile = "state.json";

    @BeforeAll
    void beforeAll() {
        launchBrowser();
        context = browser.newContext();
        page = context.newPage();
        LoginPage loginPage = new LoginPage(page);
        login(loginPage);
        context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get(stateFile)));
    }

    @AfterAll
    void afterAll() {
        File state = Paths.get(stateFile).toFile();
        if (!state.delete()) {
            System.out.println("State file wasn't deleted");
        }
        closePlaywright();
    }

    @BeforeEach
    void launchStoredState() {
        try {
            Path storedState = Paths.get(stateFile);
            context = browser.newContext(new Browser.NewContextOptions().setStorageStatePath(storedState));
            page = context.newPage();
            page.navigate("https://demoqa.com/login");
        } catch (PlaywrightException e) {
            System.out.println("NoSuchFileException - Failed to read storage state from file");
        }
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
    }

    void closePlaywright() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    void createApiRequestContext(String baseUrl, Map<String, String> headers) {
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(baseUrl)
                .setExtraHTTPHeaders(headers));
    }

    void login(LoginPage loginPage) {
        loginPage.open()
                .enterUserName()
                .enterPassword()
                .clickLogin();
        assertThat(new ProfilePage(page).getLogOutButton()).isVisible();
    }
}
