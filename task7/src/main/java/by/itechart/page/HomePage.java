package by.itechart.page;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage extends BasePage {
    private final Locator categoriesMenuItem;
    private final Locator allItemsMenuItem;
    private final Locator contentSaleSection;
    private final String categoryMenuItem = "//a[@class=\"popup_menu_item\" and normalize-space() = \"%s\"]";

    public HomePage(Page page) {
        super(page);
        this.categoriesMenuItem = page.locator("//a[@class=\"pulldown_desktop\" and text()=\"Categories\"]");
        this.contentSaleSection = page.locator("#SaleSection_13268");
        this.allItemsMenuItem = page.locator("//div[text()=\"All Items\"]");
    }

    public HomePage open() {
        page.navigate("https://store.steampowered.com/");
        return this;
    }

    public void clickCategoriesMenuItem() {
        categoriesMenuItem.hover();
    }

    public HomePage clickCategory(String category) {
        page.locator(String.format(categoryMenuItem, category)).click();
        return this;
    }

    public void scrollTillContentSaleSection() {
        contentSaleSection.scrollIntoViewIfNeeded();
    }

    public void chooseCategory(String category) {
        clickCategory(category);
        scrollUntilElementIsVisible();
        scrollTillContentSaleSection();
    }

    public void scrollUntilElementIsVisible() {
        while (!(allItemsMenuItem.isVisible())) {
            page.mouse().wheel(0, 600);
        }
    }
}
