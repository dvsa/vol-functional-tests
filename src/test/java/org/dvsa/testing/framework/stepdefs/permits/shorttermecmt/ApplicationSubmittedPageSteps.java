package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ReceiptPage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;

import static org.dvsa.testing.framework.runner.Hooks.getBrowser;

public class ApplicationSubmittedPageSteps extends BasePage implements En {

    public ApplicationSubmittedPageSteps(World world)
    {
        Then("^I open the receipt and it should open in a new window$", () -> {
            SubmittedPage.openReceipt();
            String[] windows = getBrowser().get().getWindowHandles().toArray(new String[0]);
            getBrowser().get().switchTo().window(windows[1]);
            ReceiptPage.untilOnPage();
            getBrowser().get().switchTo().window(windows[0]);
        });
    }
}