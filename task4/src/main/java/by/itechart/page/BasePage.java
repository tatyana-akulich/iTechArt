package by.itechart.page;

import com.microsoft.playwright.Page;

public interface BasePage {
    default BasePage open(Page page){
        return null;
    };
}