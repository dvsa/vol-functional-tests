package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ReceiptPage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.openqa.selenium.WebDriver;

public class ApplicationSubmittedPageSteps extends BasePage implements En {

    public ApplicationSubmittedPageSteps(World world)
    {
        Then("^I open the receipt and it should open in a new window$", () -> {
            WebDriver driver = getDriver();
            SubmittedPage.openReceipt();
            String[] windows = driver.getWindowHandles().toArray(new String[0]);
            driver.switchTo().window(windows[1]);
            ReceiptPage.untilOnPage();
            driver.switchTo().window(windows[0]);
        });
    }
}
