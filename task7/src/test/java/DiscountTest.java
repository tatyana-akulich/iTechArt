import by.itechart.page.*;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscountTest extends BaseTest {
    Page steam;
    HomePage homePage;
    HomeContentPage contentPage;
    SteamDetailsPage detailsPage;
    AgeCheckPage ageCheckPage;
    SteamInstallPage installPage;
    String steamTitle;
    String priceOnContentPage;
    String priceOnDetailsPage;
    String discountOnDetailsPage;
    int maxDiscount;
    String maxDiscountOnContentPage;

    @Test
    public void testInstallSteamWithDiscount() {
        homePage = new HomePage(mainPage);
        contentPage = new HomeContentPage(mainPage);
        homePage.open().chooseCategoriesMenuItem();
        String categoryName = Categories.Anime.getTitle();  //choose Category from Enum
        chooseCategory(categoryName);

        contentPage.clickNewAndTrending();
        mainPage.waitForCondition(() -> contentPage.countPreviewWidgetPhotos() > 1);

        List<String> discounts = new ArrayList<>();
        for (Locator steamItem : contentPage.getSteamsWithDiscounts()) {
            discounts.add(steamItem.textContent());
        }

        if (discounts.size() > 0) {
            openFirstSteamWithMaxDiscount(discounts);
        } else {
            openFirstSteamWithMaxPrice();
        }

        detailsPage = new SteamDetailsPage(steam);
        ageCheckPage = new AgeCheckPage(steam);
        installPage = new SteamInstallPage(steam);
        steam.waitForLoadState(LoadState.LOAD);

        fillCheckAgeBlock();

        steamTitle = detailsPage.getSteamTitle();
        String buyWithTitle = "Buy " + steamTitle;
        if (maxDiscount > 0) {
            discountOnDetailsPage = detailsPage.getSteamDiscount(buyWithTitle);
            priceOnDetailsPage = detailsPage.getSteamPrice(buyWithTitle);
            assertEquals(discountOnDetailsPage, maxDiscountOnContentPage);
            assertEquals(priceOnContentPage + " USD", priceOnDetailsPage);
        } else {
            priceOnDetailsPage = detailsPage.getPriceForSteamWithoutDiscount(buyWithTitle);
            assertEquals(priceOnContentPage + " USD", priceOnDetailsPage);
        }
        downloadSteam();
    }

    public void downloadSteam() {
        detailsPage.clickInstallSteam();
        Download download = steam.waitForDownload(installPage::clickInstallSteam);
        String fileName = "downloads/" + download.suggestedFilename();
        download.saveAs(Paths.get(fileName));
        File downloadedFile = new File(fileName);
        File renamed = new File(fileName.substring(0, fileName.indexOf(".")) +
                new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".exe");
        if (downloadedFile.renameTo(renamed)) {
            System.out.printf("File %s was renamed to %s%n", fileName, renamed);
        }
    }

    public void chooseCategory(String category) {
        homePage.chooseCategory(category);
        mainPage.waitForLoadState(LoadState.LOAD);
        mainPage.mouse().wheel(0, 3000);
    }

    public void openFirstSteamWithMaxDiscount(List<String> discounts) {
        List<Integer> discountsInt = new ArrayList<>();
        for (String element : discounts
        ) {
            discountsInt.add(Integer.parseInt(element.substring(1, element.length() - 1)));
        }
        maxDiscount = Collections.max(discountsInt);
        maxDiscountOnContentPage = "-" + maxDiscount + "%";
        priceOnContentPage = contentPage.getFirstPriceWithMaxDiscount(maxDiscountOnContentPage);
        steam = context.waitForPage(() -> {
            contentPage.openSteamByDiscount(maxDiscountOnContentPage);
        });
    }

    public void openFirstSteamWithMaxPrice() {
        List<String> prices = new ArrayList<>();
        for (Locator steamItem : contentPage.getSteamsWithPrice()) {
            prices.add(steamItem.textContent());
        }
        List<Double> pricesDouble = new ArrayList<>();
        for (String element : prices
        ) {
            if (element.matches("\\$[\\d]+\\.?[\\d]*")) {
                pricesDouble.add(Double.parseDouble(element.substring(1)));
            }
        }
        double maxPrice = Collections.max(pricesDouble);
        priceOnContentPage = "$" + String.format(Locale.US, "%.02f", maxPrice);
        steam = context.waitForPage(() -> {
            contentPage.openSteamByPrice(priceOnContentPage);
        });
    }

    public void fillCheckAgeBlock() {
        if (ageCheckPage.getAgeControlText().isVisible()) {
            ageCheckPage.chooseYearOfBirth("2000").clickViewPage();
        }
    }
}

