package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HeaderPage extends BasePage {
    private final Locator installSteamButton;

    public HeaderPage(Page page) {
        super (page);
        this.installSteamButton = page.locator("//a[@class = \"about_install_steam_link\" and text()=\"Install Steam\"]");
    }

    public void clickInstallSteam() {
        installSteamButton.nth(0).click();
    }
}
