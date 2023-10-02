package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class BookDescriptionPage implements BasePage {
    private final Page page;
    private final Locator amountOfPagesLabel;

    public BookDescriptionPage(Page page) {
        this.page = page;
        this.amountOfPagesLabel = page.locator("//div[@id='pages-wrapper']//label[@id='userName-value']");
    }

    public Locator getAmountOfPagesLocator() {
        return amountOfPagesLabel;
    }
}
