package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HeaderPage extends BasePage {
    private final Locator installSteamButton;

    public HeaderPage(Page page) {
        super(page);
        this.installSteamButton = page.locator("//div[normalize-space() = \"Install Steam\"]");
    }

    public void clickInstallSteam() {
        log.info("Open steam install page");
        installSteamButton.nth(0).click();
    }
}
