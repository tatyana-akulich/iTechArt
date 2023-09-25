package api;

import com.google.gson.Gson;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.Cookie;
import org.example.dto.Book;
import org.example.dto.GetBooks;
import org.example.dto.GetUserId;
import org.junit.jupiter.api.*;
import org.example.util.PropertiesLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MainTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    private APIRequestContext request;
    String token;

    @BeforeAll
    void beforeAll() {
        launchBrowser();
    }

    @AfterAll
    void afterAll() {
        disposeAPIRequestContext();
        closePlaywright();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
    }

    void disposeAPIRequestContext() {
        if (request != null) {
            request.dispose();
            request = null;
        }
    }

    void closePlaywright() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    @Test
    void testAPI() throws IOException {
        page.navigate("https://demoqa.com/login");
        login();
        checkAndSaveCookies();
        page.route("**/*.{png,jpg,jpeg}", Route::abort);
        waitForResponseAndCheckResult();
        changeAmountOfPagesWithRoute();
        checkBooksWithUserId();
    }

    void login() {
        Properties properties = PropertiesLoader.loadProperties();
        page.locator("#userName").fill(properties.getProperty("userName"));
        page.locator("#password").fill(properties.getProperty("password"));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log out"))).isVisible();
    }

    void checkAndSaveCookies() throws IOException {
        List<Cookie> cookies = context.cookies();
        Map<String, String> cookiesNamesAndValues = new HashMap<>();
        for (Cookie element : cookies
        ) {
            cookiesNamesAndValues.put(element.name, element.value);
        }
        List<String> cookiesToCheck = List.of("userID", "userName", "expires", "token");
        for (String element : cookiesToCheck
        ) {
            assertThat(cookiesNamesAndValues).containsKey(element);
            assertThat(cookiesNamesAndValues.get(element)).isNotEmpty();
        }
        Properties properties = PropertiesLoader.loadProperties();
        properties.setProperty("userID", cookiesNamesAndValues.get("userID"));
        token = cookiesNamesAndValues.get("token");
        properties.store(Files.newOutputStream(Paths.get("src/test/resources/authentication.properties")),
                "added userID and token");
    }

    void waitForResponseAndCheckResult() {
        Response response = page.waitForResponse(r -> r.url().equals("https://demoqa.com/BookStore/v1/Books")
                && r.request().method().equals("GET"), () -> {
            page.locator("//span[@class='text' and text()= 'Book Store']").click();
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshot.png"))
                    .setFullPage(true));
        });
        assertTrue(response.ok());
        GetBooks result = new Gson().fromJson(response.text(), GetBooks.class);
        int amountOfBooksUI = page.locator(".mr-2").count();
        assertThat(result.getBooks().size()).isEqualTo(amountOfBooksUI);
    }

    void changeAmountOfPagesWithRoute() {
        int newAmountOfPages = new Random().nextInt(1000);
        page.route("https://demoqa.com/BookStore/v1/Book**", route -> {
            if (route.request().method().equals("GET")) {
                APIResponse response = route.fetch();
                Book result = new Gson().fromJson(response.text(), Book.class);
                result.setPages(newAmountOfPages);
                route.fulfill(new Route.FulfillOptions()
                        .setResponse(response)
                        .setBody(result.toString()));
            }
        });
        page.locator("//span[@id='see-book-Learning JavaScript Design Patterns']").click();
        String amountOfPagesUI = (page.locator("//div[@id='pages-wrapper']//label[@id='userName-value']")
                .textContent());
        assertThat(amountOfPagesUI).isEqualTo(String.valueOf(newAmountOfPages));
    }

    void checkBooksWithUserId() {
        Map<String, String> headers = new HashMap<>();
        Properties properties = PropertiesLoader.loadProperties();
        headers.put("Authorization", "Bearer " + token);
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://demoqa.com")
                .setExtraHTTPHeaders(headers));
        String path = String.format("/Account/v1/User/%s", properties.getProperty("userID"));
        APIResponse userInfo = request.get(path);
        assertTrue(userInfo.ok());
        GetUserId result = new Gson().fromJson(userInfo.text(), GetUserId.class);
        assertThat(result.getUsername()).isEqualTo(properties.getProperty("userName"));
        assertThat(result.getBooks()).isEmpty();
    }
}
