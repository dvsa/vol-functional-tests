package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.TimeoutException;

import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RefundInterim extends BasePage {
    private final World world;

    public RefundInterim (World world) {this.world = world;}


    @When("the interim fee has been paid")
    public void theInterimFeeHasBeenPaid() throws HttpException {
        world.grantApplication.getOutstandingFees();
        world.grantApplication.payOutstandingFees();
    }

    @And("the application has been refused")
    public void theApplicationHasBeenRefused() throws HttpException {
        world.grantApplication.refuse(world.createApplication.getApplicationId());
    }

    @Then("the interim fee should be refunded")
    public void theInterimFeeShouldBeRefunded() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        final String EXPECTED_AMOUNT = "£68.00";
        final String REFUNDED_TEXT = "refunded";
        final long TIMEOUT_MILLIS = 50_000;

        waitAndClickByLinkText("Fees");
        selectValueFromDropDown("//*[@id='status']", SelectorType.XPATH, "All");
        waitForTextToBePresent(EXPECTED_AMOUNT);
        waitAndClickByLinkText("Grant Interim Fee for application");
        waitForTextToBePresent("Fee details");

        long timeoutDeadline = System.currentTimeMillis() + TIMEOUT_MILLIS;

        while (System.currentTimeMillis() < timeoutDeadline) {
            refreshPageWithJavascript();
            String feeStatus = getText("//*//dd//strong", SelectorType.XPATH).toLowerCase();
            if (feeStatus.contains(REFUNDED_TEXT)) {
                break;
            }
        }
        if (System.currentTimeMillis() >= timeoutDeadline) {
            throw new TimeoutException("Timeout while waiting for the interim fee to be refunded.");
        }
        assertTrue(getText("//*//dd//strong", SelectorType.XPATH).toLowerCase().contains(REFUNDED_TEXT));
        assertTrue(checkForPartialMatch(EXPECTED_AMOUNT));
    }

    @And("the application has been withdrawn")
    public void theApplicationHasBeenWithdrawn() throws HttpException {
        world.grantApplication.withdraw(world.createApplication.getApplicationId());

    }

    @And("the licence is granted")
    public void theLicenceIsGranted() throws HttpException {
        world.APIJourney.grantLicenceAndPayFees();
    }

    @And("the interim is granted")
    public void theInterimIsGranted() throws HttpException {
        world.updateLicence.grantInterimApplication(world.createApplication.getApplicationId());
    }

    @Then("the interim fee should not be refunded")
    public void theInterimFeeShouldNotBeRefunded() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        waitAndClickByLinkText("Fees");
        do {
            waitAndClick("//*[@id=\"status\"]/option[@value='all']", SelectorType.XPATH);
        } while (!isTextPresent("Paid"));
        assertTrue(checkForPartialMatch("£68.00"));
        assertFalse(world.genericUtils.returnFeeStatus("CANCELLED"));
    }

    @When("i pay for the interim application")
    public void iPayForTheInterimApplication() {
        world.internalUIJourney.payForInterimApp();
        waitForTitleToBePresent("Application overview");
    }

    @And("the variation application has been refused")
    public void theVariationApplicationHasBeenRefused() throws HttpException {
        world.grantApplication.refuse(world.updateLicence.getVariationApplicationId());
    }

    @And("the variation application has been withdrawn")
    public void theVariationApplicationHasBeenWithdrawn() throws HttpException {
        world.grantApplication.withdraw(world.updateLicence.getVariationApplicationId());
    }

    @And("the variation interim is granted")
    public void theVariationInterimIsGranted() throws HttpException {
        world.updateLicence.grantInterimApplication(world.updateLicence.getVariationApplicationId());
    }
}