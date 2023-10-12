package by.itechart.page;

import by.itechart.dto.Book;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.function.BooleanSupplier;

public class BookStorePage implements BasePage {

    private final Page page;
    private final Locator listOfBookTitles;
    private final Locator bookTitleText;
    private final Locator authorText;
    private final Locator publisherText;

    public BookStorePage(Page page) {
        this.page = page;
        this.listOfBookTitles = page.locator(".mr-2");
        this.bookTitleText = page.locator("(//span[@class=\"mr-2\"])/a");
        this.authorText = page.locator("(//span[@class=\"mr-2\"])/following::div[1]");
        this.publisherText = page.locator("(//span[@class=\"mr-2\"])/following::div[2]");
    }

    public Locator getAllBooksTitlesLocator() {
        page.waitForCondition(() -> listOfBookTitles.count() > 0);
        return listOfBookTitles;
    }

    public void chooseBookByNumber(int number) {
        listOfBookTitles.nth(number - 1).click();
    }

    public Book getBookByNumber(int number) {
        return Book.builder()
                .title(bookTitleText.nth(number - 1).textContent())
                .author(authorText.nth(number - 1).textContent())
                .publisher(publisherText.nth(number - 1).textContent())
                .build();
    }
}
