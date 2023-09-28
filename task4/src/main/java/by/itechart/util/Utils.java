package by.itechart.util;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.Cookie;

import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class Utils {
    static int screenshotNumber = 1;

    public static void takeScreenShot(Page page) {
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("screenshot" + screenshotNumber + ".png"))
                .setFullPage(true));
        screenshotNumber++;
    }

    public static int generateRandomInt(int bound) {
        return new Random().nextInt(bound) + 1;
    }

    public static Cookie getCookieByName(BrowserContext context, String name) {
        List<Cookie> cookies = context.cookies();
        for (Cookie element : cookies
        ) {
            if (element.name.equals(name)) {
                return element;
            }
        }
        return null;
    }
}
