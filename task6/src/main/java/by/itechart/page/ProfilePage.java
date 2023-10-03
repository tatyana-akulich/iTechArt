package by.itechart.page;

import by.itechart.dto.Book;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.HashSet;
import java.util.Set;

public class ProfilePage implements BasePage {
    private final Page page;
    private final Locator logOutButton;
    private final Locator listOfBookTitles;
    private final Locator deleteAllBooksButton;
    private final Locator deleteRecordIcon;

    public ProfilePage(Page page) {
        this.page = page;
        this.logOutButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log out"));
        this.listOfBookTitles = page.locator(".mr-2");
        this.deleteAllBooksButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete All Books"));
        this.deleteRecordIcon = page.locator("#delete-record-undefined");
    }

    public Locator getLogOutButton() {
        return logOutButton;
    }

    public Locator getAllBooksLocator() {
        return listOfBookTitles;
    }

    public Locator getBookByNumberLocator(int number) {
        return listOfBookTitles.nth(number - 1);
    }

    public void clickDeleteAllBooks() {
        deleteAllBooksButton.click();
    }

    public Set<Book> getAllBooks(int amountOfBooks) {
        Set<Book> booksInProfile = new HashSet<>();
        for (int i = 1; i <= amountOfBooks; i++) {
            booksInProfile.add(Book.builder()
                    .title(page.locator(String.format("(//span[@class=\"mr-2\"])[%d]/a", i)).textContent())
                    .author(page.locator
                            (String.format("(//span[@class=\"mr-2\"])[%d]/following::div[1]", i)).textContent())
                    .publisher(page.locator
                            (String.format("(//span[@class=\"mr-2\"])[%d]/following::div[2]", i)).textContent())
                    .build());
        }
        return booksInProfile;
    }

    public void deleteBookByNumber(int number) {
        deleteRecordIcon.nth(number - 1).click();
    }
}
