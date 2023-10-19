package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public class SteamDetailsPage extends BasePage {
    private final Locator installSteamButton;
    private final Locator pricesWithoutDiscount;
    private final Locator pricesWithDiscount;
    private final Locator allDiscounts;

    public SteamDetailsPage(Page page) {
        super (page);
        this.installSteamButton = page.locator("//div[normalize-space() = \"Install Steam\"]");
        this.pricesWithoutDiscount = page.locator(".game_purchase_price");
        this.pricesWithDiscount = page.locator(".game_area_purchase .discount_final_price");
        this.allDiscounts = page.locator(".game_area_purchase .discount_pct");
    }

    public void clickInstallSteam() {
        installSteamButton.click();
    }

    public List<String> getAllPrices() {
        List<String> allPrices = pricesWithDiscount.allTextContents();
        allPrices.addAll(pricesWithoutDiscount.allTextContents());
        return allPrices;
    }

    public Locator getAllDiscountsLocator() {
        return allDiscounts;
    }
}
