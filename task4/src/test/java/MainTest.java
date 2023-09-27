import by.itechart.dto.Book;
import by.itechart.dto.GetBooks;
import by.itechart.dto.GetUserId;
import by.itechart.page.BookDescriptionPage;
import by.itechart.page.BookStorePage;
import by.itechart.page.LoginPage;
import by.itechart.page.MenuPage;
import by.itechart.util.PropertiesLoader;
import by.itechart.util.Utils;
import com.google.gson.Gson;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MainTest extends BaseTest {
    private APIRequestContext request;
    String token;
    String userID;

    @Test
    void testAPI() {
        LoginPage loginPage = new LoginPage(page);
        login(loginPage);
        checkAndSaveCookies();
        page.route("**/*.{png,jpg,jpeg}", Route::abort);
        waitForResponseAndCheckResult();
        changeAmountOfPagesWithRoute();
        checkBooksWithUserId();
        disposeAPIRequestContext();
    }

    void login(LoginPage loginPage) {
        loginPage.open()
                .enterUserName()
                .enterPassword()
                .clickLogin();
        assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log out"))).isVisible();
    }

    void checkAndSaveCookies() {
        Cookie cookie;
        List<String> cookiesToCheck = List.of("userID", "userName", "expires", "token");
        for (String element : cookiesToCheck
        ) {
            cookie = Utils.getCookieByName(context, element);
            assertThat(cookie).isNotNull();
            assertThat(cookie.value).isNotEmpty();
        }
        token = Utils.getCookieByName(context, "token").value;
        userID = Utils.getCookieByName(context, "userID").value;
    }

    void waitForResponseAndCheckResult() {
        Response response = page.waitForResponse(r -> r.url().equals("https://demoqa.com/BookStore/v1/Books")
                && r.request().method().equals("GET"), () -> {
            new MenuPage(page).openBookStore();
            Utils.takeScreenShot(page);
        });
        assertTrue(response.ok());
        GetBooks result = new Gson().fromJson(response.text(), GetBooks.class);
        assertThat(new BookStorePage(page).findAllBooks()).hasCount(result.getBooks().size());
    }

    void changeAmountOfPagesWithRoute() {
        int newAmountOfPages = Utils.generateRandomInt(1000);
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
        BookStorePage bookStorePage = new BookStorePage(page);
        int randomNumberOfBook = Utils.generateRandomInt(bookStorePage.findAllBooks().count());
        bookStorePage.chooseBookByNumber(randomNumberOfBook);
        assertThat(new BookDescriptionPage(page).getAmountOfPages()).hasText(String.valueOf(newAmountOfPages));
    }

    void checkBooksWithUserId() {
        Map<String, String> headers = new HashMap<>();
        Properties properties = PropertiesLoader.loadProperties();
        headers.put("Authorization", "Bearer " + token);
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://demoqa.com")
                .setExtraHTTPHeaders(headers));
        String path = String.format("/Account/v1/User/%s", userID);
        APIResponse userInfo = request.get(path);
        assertTrue(userInfo.ok());
        GetUserId result = new Gson().fromJson(userInfo.text(), GetUserId.class);
        assertThat(result.getUsername()).isEqualTo(properties.getProperty("userName"));
        assertThat(result.getBooks()).isEmpty();
    }

    void disposeAPIRequestContext() {
        if (request != null) {
            request.dispose();
            request = null;
        }
    }
}
