package by.itechart.page;

public interface BasePage {
    default BasePage open(){
        return null;
    };

    default boolean isOpened(){
        return true;
    }
}