package by.itechart.page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AgeCheckPage implements BasePage {
    private final Page page;
    private final Locator viewPageButton;
    private final Locator ageControlText;
    private final Locator yearOfBirthSelect;

    public AgeCheckPage(Page page) {
        this.page = page;
        this.ageControlText = page.locator("//div[text()=\"Please enter your birth date to continue:\"]");
        this.yearOfBirthSelect = page.locator("#ageYear");
        this.viewPageButton = page.getByText("View Page");
    }

    public Locator getAgeControlText() {
        return ageControlText;
    }

    public AgeCheckPage chooseYearOfBirth(String year) {
        yearOfBirthSelect.selectOption(year);
        return this;
    }

    public AgeCheckPage clickViewPage() {
        viewPageButton.click();
        return this;
    }
}
