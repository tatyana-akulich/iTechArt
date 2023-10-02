package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MenuPage implements BasePage {
    private final Page page;
    private final Locator bookStoreListItem;
    private final Locator profileListItem;

    public MenuPage(Page page) {
        this.page = page;
        this.bookStoreListItem = page.locator("//span[@class='text' and text()= 'Book Store']");
        this.profileListItem = page.locator("//span[@class='text' and text()= 'Profile']");
    }

    public void openBookStore() {
        bookStoreListItem.click();
    }

    public void openProfile() {
        profileListItem.click();
    }
}
