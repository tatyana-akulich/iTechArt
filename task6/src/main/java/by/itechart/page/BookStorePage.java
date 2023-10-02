package by.itechart.page;

import by.itechart.dto.Book;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class BookStorePage implements BasePage {

    private final Page page;
    private final Locator listOfBookTitles;
    private final String bookTitleLocator = "(//span[@class=\"mr-2\"])[%s]";

    public BookStorePage(Page page) {
        this.page = page;
        this.listOfBookTitles = page.locator(".mr-2");
    }

    public Locator getAllBooksTitlesLocator() {
        return listOfBookTitles;
    }

    public void chooseBookByNumber(int number) {
        page.locator(String.format(bookTitleLocator, number)).click();
    }

    public Book getBookByNumber(int number) {
        return Book.builder()
                .title(page.locator(String.format("(//span[@class=\"mr-2\"])[%d]/a", number)).textContent())
                .author(page.locator
                        (String.format("(//span[@class=\"mr-2\"])[%d]/following::div[1]", number)).textContent())
                .publisher(page.locator
                        (String.format("(//span[@class=\"mr-2\"])[%d]/following::div[2]", number)).textContent())
                .build();
    }
}
