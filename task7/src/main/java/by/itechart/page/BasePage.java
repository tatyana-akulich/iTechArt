package by.itechart.page;

import com.microsoft.playwright.Page;

public class BasePage {
    Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    BasePage open() {
        return null;
    }
    boolean isOpened() {
        return true;
    }
}