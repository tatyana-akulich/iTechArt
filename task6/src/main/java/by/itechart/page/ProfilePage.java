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
    private final Locator modalTitleText;
    private final Locator modalBodyText;
    private final Locator okModalButton;
    private final Locator cancelModalButton;
    private final Locator deleteAllBooksButton;
    private final String deleteIconLocator = "(//span[@id='delete-record-undefined'])[%d]";
    private final String bookTitleLocator = "(//span[@class=\"mr-2\"]/a)[%d]";

    public ProfilePage(Page page) {
        this.page = page;
        this.logOutButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log out"));
        this.listOfBookTitles = page.locator(".mr-2");
        this.modalTitleText = page.locator("//div[@class='modal-title h4']");
        this.modalBodyText = page.locator(".modal-body");
        this.okModalButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("OK"));
        this.cancelModalButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cancel"));
        this.deleteAllBooksButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete All Books"));

    }

    public Locator getLogOutButton() {
        return logOutButton;
    }

    public Locator getAllBooksLocator() {
        return listOfBookTitles;
    }

    public Locator getOkModalButtonLocator() {
        return okModalButton;
    }

    public Locator getBookByNumberLocator(int number) {
        return page.locator(String.format(bookTitleLocator, number));
    }

    public Locator getModalTitleTextLocator() {
        return modalTitleText;
    }

    public Locator getModalBodyTextLocator() {
        return modalBodyText;
    }

    public void clickOkModalButton() {
        okModalButton.click();
    }

    public void clickDeleteAllBooks() {
        deleteAllBooksButton.click();
    }

    public Locator getCancelModalButtonLocator() {
        return cancelModalButton;
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
        page.locator(String.format(deleteIconLocator, number)).click();
    }
}
