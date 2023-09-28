package by.itechart.util;

import by.itechart.dto.Book;
import com.google.gson.Gson;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;

public class Router {
    public static void abortImages(Page page) {
        page.route("**/*.{png,jpg,jpeg}", Route::abort);
    }

    public static void changeAmountOfPagesInBooks(Page page, int newAmountOfPages) {
        page.route("https://demoqa.com/BookStore/v1/Book**", route -> {
            if (route.request().method().equals("GET")) {
                APIResponse response = route.fetch();
                Book result = new Gson().fromJson(response.text(), Book.class);
                result.setPages(newAmountOfPages);
                route.fulfill(new Route.FulfillOptions()
                        .setResponse(response)
                        .setBody(result.toString()));
            }
        });
    }
}
