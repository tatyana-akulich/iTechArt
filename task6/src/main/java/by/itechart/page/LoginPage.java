package by.itechart.page;

import by.itechart.util.PropertiesLoader;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.Properties;

public class LoginPage implements BasePage {
    private final Page page;
    private final Locator userNameInput;
    private final Locator passwordInput;
    private final Locator loginButton;

    public LoginPage(Page page) {
        this.page = page;
        this.userNameInput = page.locator("#userName");
        this.passwordInput = page.locator("#password");
        this.loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
    }

    public LoginPage open() {
        page.navigate("https://demoqa.com/login");
        return this;
    }

    Properties loadProperties() {
        return PropertiesLoader.loadProperties();
    }

    public LoginPage enterUserName() {
        userNameInput.fill(loadProperties().getProperty("userName"));
        return this;
    }

    public LoginPage enterPassword() {
        passwordInput.fill(loadProperties().getProperty("password"));
        return this;
    }

    public LoginPage clickLogin() {
        loginButton.click();
        return this;
    }
}
