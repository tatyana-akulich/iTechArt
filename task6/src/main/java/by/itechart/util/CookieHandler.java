package by.itechart.util;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.options.Cookie;

import java.util.List;

public class CookieHandler {
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
