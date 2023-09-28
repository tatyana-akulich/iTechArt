package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ProfilePage implements BasePage {
    private final Page page;
    private final Locator logOutButton;

    public ProfilePage(Page page) {
        this.page = page;
        this.logOutButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log out"));
    }

    public Locator getLogOutButton() {
        return logOutButton;
    }
}
