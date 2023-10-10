package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class SteamDetailsPage implements BasePage {
    private final Page page;
    private final Locator installSteamButton;
    private final Locator steamTitleText;
    private final String steamDiscount = "//h1[contains(text(), \"%s\")]/following-sibling::" +
            "div//div [@class=\"discount_pct\"]";
    private final String steamPrice = "//h1[contains(text(), \"%s\")]/following-sibling::" +
            "div//div [@class=\"discount_final_price\"]";
    private final String steamPriceWithoutDiscount = "//h1[contains(text(), \"%s\")]/following-sibling::" +
            "div//div [@class=\"game_purchase_price price\"]";

    public SteamDetailsPage(Page page) {
        this.page = page;
        this.installSteamButton = page.locator("//div[normalize-space() = \"Install Steam\"]");
        this.steamTitleText = page.locator("#appHubAppName");
    }

    public void clickInstallSteam() {
        installSteamButton.click();
    }

    public String getSteamTitle() {
        return steamTitleText.textContent();
    }

    public String getSteamDiscount(String title) {
        return page.locator(String.format(steamDiscount, title)).nth(0).textContent();
    }

    public String getSteamPrice(String title) {
        return page.locator(String.format(steamPrice, title)).nth(0).textContent();
    }

    public String getPriceForSteamWithoutDiscount(String title) {
        return page.locator(String.format(steamPriceWithoutDiscount, title)).nth(0).textContent().trim();
    }
}
