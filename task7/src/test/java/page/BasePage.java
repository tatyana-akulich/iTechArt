package page;

import com.microsoft.playwright.Page;
import org.apache.logging.log4j.Logger;
import test.BaseTest;

public class BasePage {
    Page page;
    Logger log;
    public BasePage(Page page) {
        this.page = page;
        log = BaseTest.getLog();
    }

    BasePage open() {
        return null;
    }

    boolean isOpened() {
        return true;
    }
}