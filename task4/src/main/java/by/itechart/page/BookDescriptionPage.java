package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class BookDescriptionPage implements BasePage {
    Page page;
    String amountOfPagesLocator = "//div[@id='pages-wrapper']//label[@id='userName-value']";

    public BookDescriptionPage(Page page) {
        this.page = page;
    }

    public Locator getAmountOfPages() {
        return page.locator(amountOfPagesLocator);
    }
}
