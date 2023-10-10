package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public class HomeContentPage implements BasePage {
    private final Page page;
    private final Locator newAndTrendingMenuItem;
    private final Locator previewWidgetImage;
    private final Locator discountText;
    private final Locator priceText;
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
    }

    public void clickNewAndTrending() {
        newAndTrendingMenuItem.click();
    }

    public int countPreviewWidgetPhotos() {
        return previewWidgetImage.count();
    }

    public List<Locator> getSteamsWithDiscounts() {
        return discountText.all();
    }

    public List<Locator> getSteamsWithPrice() {
        return priceText.all();
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
}
