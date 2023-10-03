package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class DeleteConfirmationPage implements BasePage {
    private final Page page;
    private final Locator modalTitleText;
    private final Locator modalBodyText;
    private final Locator okModalButton;
    private final Locator cancelModalButton;

    public DeleteConfirmationPage(Page page) {
        this.page = page;
        this.modalTitleText = page.locator("//div[@class='modal-title h4']");
        this.modalBodyText = page.locator(".modal-body");
        this.okModalButton = page.locator("#closeSmallModal-ok");
        this.cancelModalButton = page.locator("#closeSmallModal-cancel");
    }

    public Locator getOkModalButtonLocator() {
        return okModalButton;
    }

    public Locator getModalTitleTextLocator() {
        return modalTitleText;
    }

    public Locator getModalBodyTextLocator() {
        return modalBodyText;
    }

    public void clickOkModalButton() {
        okModalButton.click();
    }

    public Locator getCancelModalButtonLocator() {
        return cancelModalButton;
    }
}
