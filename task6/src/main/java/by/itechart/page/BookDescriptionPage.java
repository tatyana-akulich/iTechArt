package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class BookDescriptionPage implements BasePage {
    private final Page page;
    private final Locator amountOfPagesLabel;
    private final Locator addToCollectionButton;
    private final Locator backToBookStoreButton;

    public BookDescriptionPage(Page page) {
        this.page = page;
        this.amountOfPagesLabel = page.locator("//div[@id='pages-wrapper']//label[@id='userName-value']");
        this.addToCollectionButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName("Add To Your Collection"));
        this.backToBookStoreButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName("Back To Book Store"));
    }

    public Locator getAmountOfPagesLocator() {
        return amountOfPagesLabel;
    }

    public void addBookToCollection() {
        addToCollectionButton.click();
    }

    public void goBackToBookStore() {
        backToBookStoreButton.click();
    }
}
