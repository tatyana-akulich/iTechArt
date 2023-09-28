package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class BookStorePage implements BasePage{

    private final Page page;
    private final Locator listOfBookTitles;
    private final String bookTitleLocator = "(//span[@class=\"mr-2\"])[%s]";

    public BookStorePage(Page page) {
        this.page = page;
        this.listOfBookTitles = page.locator(".mr-2");
    }

    public Locator getAllBooksTitlesLocator(){
        return listOfBookTitles;
    }

    public void chooseBookByNumber(int number){
        page.locator(String.format(bookTitleLocator, number)).click();
    }
}
