package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import activesupport.driver.Browser;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ReceiptPage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;


public class ApplicationSubmittedPageSteps extends BasePage implements En {

    public ApplicationSubmittedPageSteps(World world)
    {
        Then("^I open the receipt and it should open in a new window$", () -> {
            SubmittedPage.openReceipt();
            String[] windows = Browser.navigate().getWindowHandles().toArray(new String[0]);
            Browser.navigate().switchTo().window(windows[1]);
            ReceiptPage.untilOnPage();
            Browser.navigate().switchTo().window(windows[0]);
        });
    }
}