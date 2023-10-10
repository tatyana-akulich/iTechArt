package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage implements BasePage {
    private final Page page;
    private final Locator categoriesMenuItem;
    private final String categoryMenuItem = "//a[@class=\"popup_menu_item\" and normalize-space() = \"%s\"]";

    public HomePage(Page page) {
        this.page = page;
        this.categoriesMenuItem = page.locator("//a[@class=\"pulldown_desktop\" and text()=\"Categories\"]");
    }

    public HomePage open() {
        page.navigate("https://store.steampowered.com/");
        return this;
    }

    public HomePage chooseCategoriesMenuItem() {
        categoriesMenuItem.hover();
        return this;
    }

    public void chooseCategory(String category) {
        page.locator(String.format(categoryMenuItem, category)).click();
    }
}
