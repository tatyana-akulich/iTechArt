package by.itechart.page;

import com.microsoft.playwright.Page;

public class MenuPage implements BasePage {
    Page page;

    public MenuPage(Page page) {
        this.page = page;
    }

    public BookStorePage openBookStore() {
        page.locator("//span[@class='text' and text()= 'Book Store']").click();
        return new BookStorePage(page);
    }
}
