import by.itechart.dto.Book;
import by.itechart.dto.GetUserId;
import by.itechart.page.*;
import by.itechart.util.RandomGenerator;
import com.google.gson.Gson;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Dialog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;
import java.util.Set;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileTest extends BaseTest {
    MenuPage menuPage;
    BookStorePage bookStorePage;
    BookDescriptionPage bookDescriptionPage;
    ProfilePage profilePage;
    DeleteConfirmationPage deleteConfirmationPage;

    @Test
    public void testAddDeleteBooksToProfile() {
        menuPage = new MenuPage(page);
        bookStorePage = new BookStorePage(page);
        bookDescriptionPage = new BookDescriptionPage(page);
        profilePage = new ProfilePage(page);
        deleteConfirmationPage = new DeleteConfirmationPage(page);

        clearProfile();

        menuPage.openBookStore();
        int amountOfBooksToAdd = RandomGenerator.generateInt(2, 5);
        Set<Book> booksToAdd = addRandomBooksToStore(amountOfBooksToAdd);

        page.reload();
        menuPage.openProfile();
        assertThat(profilePage.getAllBooksLocator()).hasCount(amountOfBooksToAdd);
        assertThat(profilePage.getAllBooks(amountOfBooksToAdd)).containsAll(booksToAdd);

        String bookToDelete = profilePage.getBookByNumberLocator(2).textContent();
        profilePage.deleteBookByNumber(2);
        assertThat(deleteConfirmationPage.getModalTitleTextLocator()).hasText("Delete Book");
        assertThat(deleteConfirmationPage.getModalBodyTextLocator()).hasText("Do you want to delete this book?");
        assertThat(deleteConfirmationPage.getOkModalButtonLocator()).hasText("OK");
        assertThat(deleteConfirmationPage.getCancelModalButtonLocator()).hasText("Cancel");
        deleteConfirmationPage.clickOkModalButton();
        page.onceDialog(dialog -> {
            assertEquals(dialog.message(), "Book deleted.");
            dialog.accept();
        });

        menuPage.openProfile();
        assertThat(profilePage.getAllBooksLocator()).hasCount(amountOfBooksToAdd - 1);
        assertThat(profilePage.getAllBooksLocator().allTextContents()).doesNotContain(bookToDelete);

        profilePage.clickDeleteAllBooks();
        assertThat(deleteConfirmationPage.getModalTitleTextLocator()).hasText("Delete All Books");
        assertThat(deleteConfirmationPage.getModalBodyTextLocator()).hasText("Do you want to delete all books?");
        assertThat(deleteConfirmationPage.getOkModalButtonLocator()).hasText("OK");
        assertThat(deleteConfirmationPage.getCancelModalButtonLocator()).hasText("Cancel");
        deleteConfirmationPage.clickOkModalButton();
        page.onceDialog(dialog -> {
            assertEquals(dialog.message(), "All Books deleted.");
            dialog.accept();
        });
        assertThat(profilePage.getAllBooksLocator()).hasCount(0);
    }

    Set<Book> addRandomBooksToStore(int amountOfBooksToAdd) {
        Set<Book> booksToAdd = new HashSet<>();
        int randomNumberOfBook;
        int totalAmountOfBooks = bookStorePage.getAllBooksTitlesLocator().count();
        for (int i = 1; i <= amountOfBooksToAdd; i++) {
            randomNumberOfBook = RandomGenerator.generateInt(1, totalAmountOfBooks);
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
        return booksToAdd;
    }

    public void clearProfile() {
        prepareApiRequest();
        String getBooksFromProfilePath = "/Account/v1/User/" + userID;
        APIResponse userInfo = request.get(getBooksFromProfilePath);
        assertTrue(userInfo.ok());
        GetUserId result = new Gson().fromJson(userInfo.text(), GetUserId.class);
        if (result.getBooks().size() != 0) {
            String deleteBooksFromProfilePath = "/BookStore/v1/Books/" + userID;
            APIResponse deleteBookFromProfile = request.delete(deleteBooksFromProfilePath);
            assertTrue(deleteBookFromProfile.ok());
        }
    }
}
