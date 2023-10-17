package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeContentPage extends BasePage {
    private final Locator newAndTrendingMenuItem;
    private final Locator previewWidgetImage;
    private final Locator discountText;
    private final Locator priceText;
    private final Locator dateText;
    private final String priceByDiscountText = "//div[@class=\"facetedbrowse_FacetedBrowseItems_NO-IP\"]" +
            "//div[text()=\"%s\"]/following-sibling::div/div[@class=\"salepreviewwidgets_StoreSalePriceBox_Wh0L8\"]";
    private final String titleByDiscountLink = "//div[@class=\"facetedbrowse_FacetedBrowseItems_NO-IP\"]" +
            "//div[text()=\"%s\"]/ancestor::div[@class=\"salepreviewwidgets_SaleItemBrowserRow_y9MSd\"]" +
            "//div[@class=\"salepreviewwidgets_StoreSaleWidgetHalfLeft_2Va3O\"]";
    private final String titleByPriceLink = "//div[@class=\"facetedbrowse_FacetedBrowseItems_NO-IP\"]" +
            "//div[text()=\"%s\"]/ancestor::div[@class=\"salepreviewwidgets_SaleItemBrowserRow_y9MSd\"]" +
            "//div[@class=\"salepreviewwidgets_StoreSaleWidgetHalfLeft_2Va3O\"]";

    public HomeContentPage(Page page) {
        this.page = page;
        this.newAndTrendingMenuItem = page.locator("//div[text()=\"New & Trending\"]");
        this.previewWidgetImage = page.locator("//div[@class=\"facetedbrowse_FacetedBrowseItems_NO-IP\"]" +
                "//div[@class=\"salepreviewwidgets_StoreSaleWidgetHalfLeft_2Va3O\"]");
        this.discountText = page.locator("//div[@class=\"facetedbrowse_FacetedBrowseItems_NO-IP\"]" +
                "//div[@class=\"salepreviewwidgets_StoreSaleDiscountBox_2fpFv\"]");
        this.priceText = page.locator("//div[@class=\"facetedbrowse_FacetedBrowseItems_NO-IP\"]" +
                "//div[@class=\"salepreviewwidgets_StoreSalePriceBox_Wh0L8\"]");
        this.dateText = page.locator(".facetedbrowse_FacetedBrowseItems_NO-IP .salepreviewwidgets_StoreSaleWidgetRelease_3eOdk");
    }

    public void clickNewAndTrending() {
        newAndTrendingMenuItem.click();
    }

    public List<String> getSteamsWithDiscounts() {
        return discountText.allTextContents();
    }

    public List<String> getSteamsWithPrice() {
        return priceText.allTextContents();
    }

    public String getFirstPriceWithMaxDiscount(String discountForLocator) {
        return page.locator(String.format(priceByDiscountText, discountForLocator)).nth(0).textContent();
    }

    public void openSteamByDiscount(String discountForLocator) {
        page.locator(String.format(titleByDiscountLink, discountForLocator)).nth(0).click();
    }

    public void openSteamByPrice(String priceForLocator) {
        page.locator(String.format(titleByPriceLink, priceForLocator)).nth(0).click();
    }

    public Locator getItemDateLocator() {
        return dateText;
    }

    public Double getMaxPrice() {
        List<String> prices = getSteamsWithPrice();
        List<Double> pricesDouble = new ArrayList<>();
        for (String element : prices
        ) {
            if (element.matches("\\$[\\d]+\\.?[\\d]*")) {
                pricesDouble.add(Double.parseDouble(element.substring(1)));
            }
        }
        return Collections.max(pricesDouble);
    }

    public int getMaxDiscount(List<String> discounts) {
        List<Integer> discountsInt = new ArrayList<>();
        for (String element : discounts
        ) {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(element);
            if (matcher.find()) {
                discountsInt.add(Integer.parseInt(matcher.group()));
            }
        }
        return Collections.max(discountsInt);
    }
}
