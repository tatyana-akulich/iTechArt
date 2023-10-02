import by.itechart.dto.Book;
import by.itechart.page.BookDescriptionPage;
import by.itechart.page.BookStorePage;
import by.itechart.page.MenuPage;
import by.itechart.page.ProfilePage;
import by.itechart.util.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;
import java.util.Set;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileTest extends BaseTest {
    MenuPage menuPage;
    BookStorePage bookStorePage;
    BookDescriptionPage bookDescriptionPage;
    ProfilePage profilePage;

    @Test
    public void testAddDeleteBooksToProfile() {
        menuPage = new MenuPage(page);
        bookStorePage = new BookStorePage(page);
        bookDescriptionPage = new BookDescriptionPage(page);
        profilePage = new ProfilePage(page);
        menuPage.openBookStore();
        int amountOfBooksToAdd = RandomGenerator.generateInt(4) + 1;
        Set<Book> booksToAdd = new HashSet<>();
        addRandomBooksToStore(amountOfBooksToAdd, booksToAdd);

        menuPage.openProfile();
        assertThat(profilePage.getAllBooksLocator()).hasCount(amountOfBooksToAdd);
        assertThat(profilePage.getAllBooks(amountOfBooksToAdd)).containsAll(booksToAdd);

        String bookToDelete = profilePage.getBookByNumberLocator(2).textContent();
        profilePage.deleteBookByNumber(2);
        assertThat(profilePage.getModalTitleTextLocator()).hasText("Delete Book");
        assertThat(profilePage.getModalBodyTextLocator()).hasText("Do you want to delete this book?");
        assertThat(profilePage.getOkModalButtonLocator()).isVisible();
        assertThat(profilePage.getCancelModalButtonLocator()).isVisible();
        profilePage.clickOkModalButton();
        page.onceDialog(dialog -> {
            assertEquals(dialog.message(), "Book deleted.");
            dialog.accept();
        });

        menuPage.openProfile();
        assertThat(profilePage.getAllBooksLocator()).hasCount(amountOfBooksToAdd - 1);
        assertThat(profilePage.getAllBooksLocator().allTextContents()).doesNotContain(bookToDelete);

        profilePage.clickDeleteAllBooks();
        assertThat(profilePage.getModalTitleTextLocator()).hasText("Delete All Books");
        assertThat(profilePage.getModalBodyTextLocator()).hasText("Do you want to delete all books?");
        assertThat(profilePage.getOkModalButtonLocator()).isVisible();
        assertThat(profilePage.getCancelModalButtonLocator()).isVisible();
        profilePage.clickOkModalButton();
        page.onceDialog(dialog -> {
            assertEquals(dialog.message(), "All Books deleted.");
            dialog.accept();
        });
        assertThat(profilePage.getAllBooksLocator()).hasCount(0);
    }

    void addRandomBooksToStore(int amountOfBooksToAdd, Set<Book> booksToAdd) {
        int randomNumberOfBook;
        int totalAmountOfBooks = bookStorePage.getAllBooksTitlesLocator().count();
        for (int i = 1; i <= amountOfBooksToAdd; i++) {
            randomNumberOfBook = RandomGenerator.generateInt(totalAmountOfBooks);
            if (booksToAdd.add(bookStorePage.getBookByNumber(randomNumberOfBook))) {
                bookStorePage.chooseBookByNumber(randomNumberOfBook);
                bookDescriptionPage.addBookToCollection();
                page.onceDialog(dialog -> {
                    assertEquals(dialog.message(), "Book added to your collection.");
                    dialog.accept();
                });
                bookDescriptionPage.goBackToBookStore();
            } else {
                i--;
                System.out.printf("Book number %d is already in the collection%n", randomNumberOfBook);
            }
        }
    }
}
