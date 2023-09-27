package by.itechart.page;

import by.itechart.util.PropertiesLoader;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.Properties;

public class LoginPage implements BasePage {
    Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public LoginPage open() {
        page.navigate("https://demoqa.com/login");
        return this;
    }

    Properties loadProperties() {
        return PropertiesLoader.loadProperties();
    }

    public LoginPage enterUserName() {
        page.locator("#userName").fill(loadProperties().getProperty("userName"));
        return this;
    }

    public LoginPage enterPassword() {
        page.locator("#password").fill(loadProperties().getProperty("password"));
        return this;
    }

    public LoginPage clickLogin() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        return this;
    }
}
