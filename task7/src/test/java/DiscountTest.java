import by.itechart.page.*;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscountTest extends BaseTest {
    Page steam;
    HomePage homePage;
    HomeContentPage contentPage;
    SteamDetailsPage detailsPage;
    AgeCheckPage ageCheckPage;
    HeaderPage headerPage;
    String priceOnContentPage;
    int maxDiscount;
    double maxPrice;
    String maxDiscountOnContentPage;

    @Test
    public void testInstallSteamWithDiscount() {
        homePage = new HomePage(mainPage);
        contentPage = new HomeContentPage(mainPage);
        homePage.open().clickCategoriesMenuItem();
        String categoryName = Categories.Action.getTitle();
        homePage.chooseCategory(categoryName);

        contentPage.clickNewAndTrending();
        mainPage.waitForCondition(() -> contentPage.getItemDateLocator().nth(0).textContent().contains("2023"));

        List<String> discounts = contentPage.getSteamsWithDiscounts();
        if (discounts.size() > 0) {
            maxDiscount = contentPage.getMaxDiscount(discounts);
            maxDiscountOnContentPage = "-" + maxDiscount + "%";
            priceOnContentPage = contentPage.getFirstPriceWithMaxDiscount(maxDiscountOnContentPage);
            openSteamWithMaxDiscount();
        } else {
            maxPrice = contentPage.getMaxPrice();
            priceOnContentPage = "$" + String.format(Locale.US, "%.02f", maxPrice);
            openSteamWithMaxPrice();
        }

        detailsPage = new SteamDetailsPage(steam);
        ageCheckPage = new AgeCheckPage(steam);
        headerPage = new HeaderPage(steam);
        steam.waitForLoadState(LoadState.LOAD);

        ageCheckPage.fillCheckAgeBlock();

        if (maxDiscount > 0) {
            assertThat(detailsPage.getAllDiscountsLocator().allTextContents()).contains(maxDiscountOnContentPage);
        }
        assertThat(detailsPage.getAllPrices()).contains(priceOnContentPage + " USD");

        downloadSteam();
    }

    public void downloadSteam() {
        detailsPage.clickInstallSteam();
        Download download = steam.waitForDownload(headerPage::clickInstallSteam);
        String fileName = "downloads/" + download.suggestedFilename();
        download.saveAs(Paths.get(fileName));
        File downloadedFile = new File(fileName);
        File renamed = new File(fileName.substring(0, fileName.indexOf(".")) +
                new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".exe");
        if (downloadedFile.renameTo(renamed)) {
            System.out.printf("File %s was renamed to %s%n", fileName, renamed);
        }
    }

    public void openSteamWithMaxDiscount() {
        steam = context.waitForPage(() -> {
            contentPage.openSteamByDiscount(maxDiscountOnContentPage);
        });
    }

    public void openSteamWithMaxPrice() {
        steam = context.waitForPage(() -> {
            contentPage.openSteamByPrice(priceOnContentPage);
        });
    }
}

