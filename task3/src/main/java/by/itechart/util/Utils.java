package by.itechart.util;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.Cookie;

import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class Utils {
    public static void takeScreenShot(Page page, String description) {
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("screenshot_" + description + ".png"))
                .setFullPage(true));

    }

    public static int generateRandomInt(int bound) {
        return new Random().nextInt(bound);
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
