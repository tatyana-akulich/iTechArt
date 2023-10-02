package by.itechart.util;

import com.microsoft.playwright.Page;

import java.nio.file.Paths;

public class ScreenshotTaker {
    public static void takeScreenShot(Page page, String description) {
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("screenshot_" + description + ".png"))
                .setFullPage(true));
    }
}
