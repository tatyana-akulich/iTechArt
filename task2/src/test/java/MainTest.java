import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.*;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MainTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeClass
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
    }

    @AfterClass
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeMethod
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterMethod
    void closeContext() {
        context.close();
    }

    @Test
    void testTextBox() {
        page.navigate("https://demoqa.com/text-box");
        assertThat(page).hasURL("https://demoqa.com/text-box");
        assertThat(page).hasTitle("DEMOQA");
        Locator fullName = page.getByPlaceholder("Full Name");
        fullName.fill("Ann");
        System.out.println(fullName.inputValue());
        page.getByPlaceholder("name@example.com").click();
        page.keyboard().press("Shift+A");
        Locator currentAddress = page.getByPlaceholder("Current Address");
        currentAddress.focus();
        currentAddress.fill("Test address");
        currentAddress.blur();
        assertThat(currentAddress).not().isFocused();
        Locator submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        assertThat(submitButton).isVisible();
        assertThat(submitButton).isEnabled();
        assertThat(submitButton).hasAttribute("id", "submit");
        submitButton.getAttribute("type");
        submitButton.focus();
        assertThat(submitButton).isFocused();
        submitButton.click();
    }

    @Test
    void testRadioButton() {
        page.navigate("https://demoqa.com/automation-practice-form");
        System.out.println("Amount of label fields - " + page.locator(".form-label").count());
        assertThat(page.locator("[name = gender]")).hasCount(3);
        Locator radioButtonMale = page.locator("#gender-radio-1");
        assertThat(radioButtonMale).hasValue("Male");
        assertThat(radioButtonMale).not().isChecked();
        assertThat(radioButtonMale).isEnabled();
        radioButtonMale.focus();
        radioButtonMale.check(new Locator.CheckOptions().setForce(true));
        assertThat(radioButtonMale).isChecked();
        page.getByLabel("Sports").click(new Locator.ClickOptions().setForce(true));
        page.getByLabel("Reading").setChecked(false);
    }

    @Test
    void testCheckBox() {
        page.navigate("https://demoqa.com/checkbox");
        page.locator("//button[@title='Expand all']").click();
        Locator desktopPlusDocuments = page.locator("//li//li [@class='rct-node rct-node-parent rct-node-expanded'][1]");
        System.out.println(desktopPlusDocuments.allInnerTexts());
        Locator desktop = page.locator("//input[@id = 'tree-node-desktop']/..");
        System.out.println(desktop.innerText());
        assertThat(desktop).not().isChecked();
        Locator home = page.locator("(//span[@class='rct-title'])[1]");
        assertThat(home).hasText("Home");
        assertThat(home).containsText("me");
        desktop.check(new Locator.CheckOptions().setForce(true));
        assertThat(desktop).isChecked();
    }

    @Test
    void testHover() {
        page.navigate("https://demoqa.com/menu#");
        page.locator("//li/a[text()='Main Item 2']").hover();
        page.locator("(//li/a[text()='Sub Item'])[1]").hover();
    }

    @Test
    void testInputFiles() {
        page.navigate("https://demoqa.com/upload-download");
        page.locator("#uploadFile").setInputFiles(Paths.get("myfile.txt"));
        assertThat(page.locator("#uploadedFilePath")).containsText("myfile.txt");
        page.locator("#downloadButton").click();
    }

    @Test
    void testDragAndDrop() {
        page.navigate("https://demoqa.com/droppable");
        page.locator("//div[@id ='draggable' and text()='Drag me']")
                .dragTo(page.locator("(//div[@id ='droppable']/p[text()='Drop here'])[1]"));
        assertThat(page.locator("(//div[@id ='droppable']/p)[1]")).containsText("Dropped");
    }

    @Test
    void webTables() {
        page.navigate("https://demoqa.com/webtables");
        page.locator("//select").selectOption("5 rows");
        assertThat(page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Previous"))).isDisabled();
    }
}