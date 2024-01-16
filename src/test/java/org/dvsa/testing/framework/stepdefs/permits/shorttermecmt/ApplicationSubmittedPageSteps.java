package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.driver.Browser;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ReceiptPage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;

public class ApplicationSubmittedPageSteps extends BasePage{
    private final World world;

    public ApplicationSubmittedPageSteps(World world) {
        this.world = world;
    }

    @Then("I open the receipt and it should open in a new window")
    public void iOpenTheReceiptAndItShouldOpen() {
        SubmittedPage.openReceipt();
        String[] windows = getDriver().getWindowHandles().toArray(new String[0]);
        getDriver().switchTo().window(windows[1]);
        ReceiptPage.untilOnPage();
        getDriver().switchTo().window(windows[0]);
    }
}