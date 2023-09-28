import by.itechart.dto.GetBooks;
import by.itechart.dto.GetUserId;
import by.itechart.page.*;
import by.itechart.util.*;
import com.google.gson.Gson;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Response;
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
    String token;
    String userID;
    String baseUrl = "https://demoqa.com";

    @Test
    void testAPI() {
        LoginPage loginPage = new LoginPage(page);
        login(loginPage);
        checkAndSaveCookies();
        Router.abortImages(page);
        waitForResponseAndCheckResult();
        changeAmountOfPagesWithRoute();
        checkBooksWithUserId();
    }

    void login(LoginPage loginPage) {
        loginPage.open()
                .enterUserName()
                .enterPassword()
                .clickLogin();
        assertThat(new ProfilePage(page).getLogOutButton()).isVisible();
    }

    void checkAndSaveCookies() {
        Cookie cookie;
        List<String> cookiesToCheck = List.of("userID", "userName", "expires", "token");
        for (String element : cookiesToCheck
        ) {
            cookie = CookieHandler.getCookieByName(context, element);
            assertThat(cookie).isNotNull();
            assertThat(cookie.value).isNotEmpty();
        }
        token = CookieHandler.getCookieByName(context, "token").value;
        userID = CookieHandler.getCookieByName(context, "userID").value;
    }

    void waitForResponseAndCheckResult() {
        Response response = page.waitForResponse(r -> r.url().equals("https://demoqa.com/BookStore/v1/Books")
                && r.request().method().equals("GET"), () -> {
            new MenuPage(page).openBookStore();
            ScreenshotTaker.takeScreenShot(page, "bookstore");
        });
        assertTrue(response.ok());
        GetBooks result = new Gson().fromJson(response.text(), GetBooks.class);
        assertThat(new BookStorePage(page).getAllBooksTitlesLocator()).hasCount(result.getBooks().size());
    }

    void changeAmountOfPagesWithRoute() {
        int newAmountOfPages = RandomGenerator.generateInt(1000);
        Router.changeAmountOfPagesInBooks(page, newAmountOfPages);
        BookStorePage bookStorePage = new BookStorePage(page);
        int randomNumberOfBook = RandomGenerator.generateInt(bookStorePage.getAllBooksTitlesLocator().count());
        bookStorePage.chooseBookByNumber(randomNumberOfBook);
        assertThat(new BookDescriptionPage(page).getAmountOfPagesLocator()).hasText(String.valueOf(newAmountOfPages));
    }

    void checkBooksWithUserId() {
        Map<String, String> headers = new HashMap<>();
        Properties properties = PropertiesLoader.loadProperties();
        headers.put("Authorization", "Bearer " + token);
        createApiRequestContext(baseUrl, headers);
        String path = String.format("/Account/v1/User/%s", userID);
        APIResponse userInfo = request.get(path);
        assertTrue(userInfo.ok());
        GetUserId result = new Gson().fromJson(userInfo.text(), GetUserId.class);
        assertThat(result.getUsername()).isEqualTo(properties.getProperty("userName"));
        assertThat(result.getBooks()).isEmpty();
    }
}
