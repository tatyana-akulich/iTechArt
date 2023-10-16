import by.itechart.dto.GetBooks;
import by.itechart.dto.GetUserId;
import by.itechart.page.BookDescriptionPage;
import by.itechart.page.BookStorePage;
import by.itechart.page.MenuPage;
import by.itechart.util.*;
import com.google.gson.Gson;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Properties;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class APITest extends BaseTest {
    @Test
    void testAPI() {
        checkAndSaveCookies();
        Router.abortImages(page);
        waitForResponseAndCheckResult();
        changeAmountOfPagesWithRoute();
        checkBooksWithUserId();
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
        int newAmountOfPages = RandomGenerator.generateInt(1, 1000);
        Router.changeAmountOfPagesInBooks(page, newAmountOfPages);
        BookStorePage bookStorePage = new BookStorePage(page);
        int randomNumberOfBook = RandomGenerator.generateInt(1, bookStorePage.getAllBooksTitlesLocator().count());
        bookStorePage.chooseBookByNumber(randomNumberOfBook);
        assertThat(new BookDescriptionPage(page).getAmountOfPagesLocator()).hasText(String.valueOf(newAmountOfPages));
    }

    void checkBooksWithUserId() {
        prepareApiRequest();
        String getBooksFromProfilePath = "/Account/v1/User/" + userID;
        APIResponse userInfo = request.get(getBooksFromProfilePath);
        assertTrue(userInfo.ok());
        GetUserId result = new Gson().fromJson(userInfo.text(), GetUserId.class);
        Properties properties = PropertiesLoader.loadProperties();
        assertThat(result.getUsername()).isEqualTo(properties.getProperty("userName"));
        assertThat(result.getBooks()).isEmpty();
    }
}
