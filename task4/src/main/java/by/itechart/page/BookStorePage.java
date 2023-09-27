package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public class BookStorePage implements BasePage{

    Page page;

    public BookStorePage(Page page) {
        this.page = page;
    }

    public Locator findAllBooks (){
        return page.locator(".mr-2");
    }

    public void chooseBookByNumber(int number){
        page.locator(String.format("(//span[@class=\"mr-2\"])[%s]", number)).click();
    }
}
