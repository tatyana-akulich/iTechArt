package by.itechart.page;

import com.microsoft.playwright.Page;

public interface BasePage {
    default BasePage open(){
        return null;
    };

    default boolean isOpened(){
        return true;
    }
}