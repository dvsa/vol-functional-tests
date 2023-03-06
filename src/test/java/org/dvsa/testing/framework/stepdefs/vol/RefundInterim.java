package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.UserType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.TimeoutException;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
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
        clickByLinkText("Fees");
        selectValueFromDropDown("//*[@id='status']", SelectorType.XPATH, "All");
        waitForTextToBePresent("£68.00");
        clickByLinkText("Grant Interim Fee for application");
        waitForTextToBePresent("Fee details");
        long kickoutTime = System.currentTimeMillis() + 60000;
        do {
            refreshPageWithJavascript();
        } while(!getText("//*//dd//strong", SelectorType.XPATH).toLowerCase().contains("refunded") && System.currentTimeMillis() < kickoutTime);
        if (System.currentTimeMillis() > kickoutTime) {
            throw new TimeoutException("Kickout time for expecting the interim fee to be refunded.");
        }
        assertTrue(getText("//*//dd//strong", SelectorType.XPATH).toLowerCase().contains("refunded"));
        assertTrue(checkForPartialMatch("£68.00"));
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
        clickByLinkText("Fees");
        do {
            waitAndClick("//*[@id=\"status\"]/option[@value='all']", SelectorType.XPATH);
        } while (!isTextPresent("Paid"));
        assertTrue(checkForPartialMatch("£68.00"));
        assertFalse(world.genericUtils.returnFeeStatus("CANCELLED"));
    }

    @When("i pay for the interim application")
    public void iPayForTheInterimApplication() {
        world.UIJourney.payForInterimApp();
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