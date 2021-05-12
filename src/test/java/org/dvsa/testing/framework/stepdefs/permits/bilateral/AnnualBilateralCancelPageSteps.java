package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.CancelApplicationPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.CancelConfirmationPage;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import static org.dvsa.testing.lib.pages.BasePage.isPath;
import static org.dvsa.testing.lib.pages.Driver.DriverUtils.getDriver;

public class AnnualBilateralCancelPageSteps implements En {

    public AnnualBilateralCancelPageSteps(OperatorStore operatorStore, World world) {
        And("^I click cancel application link$", () -> {
            operatorStore.getLatestLicence().get().setReferenceNumber(org.dvsa.testing.lib.pages.external.permit.bilateral.OverviewPage.reference());
            OverviewPage.Application.cancel();
        });
        Then("^the application reference number should be displayed above the heading$", () -> {
            String actualReference = CancelApplicationPage.reference();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);
        });
        Then("^the bilateral CancelApplication heading should be correct$", CancelApplicationPage::untilOnPage);

        When("^I should see the correct text displayed next to the checkbox", CancelApplicationPage::cancelConfirmationText);
        When("the checkbox is selected", CancelApplicationPage::cancel);
        Then("^the bilateral CancelApplication page displays the correct advisory text$", CancelApplicationPage::hasAdvisoryText);
        When("^the cancel application button is selected without checkbox ticked$", CancelApplicationPage::cancelApplication);
        Then ("I should be taken to cancel confirmation page", () -> {
            CancelConfirmationPage.untilOnPage();
            CancelConfirmationPage.cancellationPagereference();
            CancelConfirmationPage.hasCancellationPageAdvisoryText();
        });
        Then("the Gov.UK hyperlink opens in a new window", () -> {
            WebDriver driver = getDriver();
            String[] windows = driver.getWindowHandles().toArray(new String[0]);
            driver.switchTo().window(windows[1]);
            isPath("https://www.gov.uk/guidance/international-authorisations-and-permits-for-road-haulage");
            driver.switchTo().window(windows[0]);
        });
        And("I select cancel application button", () -> CancelApplicationPage.cancelApplication());
        //Guidance link no more displayed on the page,changed the assertion
        Then("GOV UK hyperlink on the page does not exist anymore", () -> {
            Assert.assertTrue("Unable to find gov uk hyperlink", CancelApplicationPage.govUKLink());
        });
        Then("I select finish button", CancelApplicationPage::finishButton);
        And("^I click cancel application link for bilateral application$", () -> {
            CancelApplicationPage.bilateralCancel();
        });
        And("^I am on the cancel application page for Annual Bilateral page$", () -> {
            CancelApplicationPage.untilOnPage();
        });
    }
}

