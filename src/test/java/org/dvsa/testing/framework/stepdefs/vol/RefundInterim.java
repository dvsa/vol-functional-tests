package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.UserRoles;
import apiCalls.enums.UserType;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.licence.UIJourney;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.TimeoutException;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.clickSaveAndContinue;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RefundInterim extends BasePage implements En {
    private final World world;

    public RefundInterim (World world) {this.world = world;}

    @Given("i have an interim {string} {string} application")
    public void iHaveAnInterimApplication(String operatorType, String licenceType) throws Exception {
        if (operatorType.equals("public")){
            throw new Exception("PSV licences cannot have interim applications.");
        }
        world.createApplication.setOperatorType(operatorType);
        world.createApplication.setLicenceType(licenceType);
        world.createApplication.setIsInterim("Y");
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        if(licenceType.equals("special_restricted") && (world.createApplication.getApplicationId() == null)){
            world.APIJourney.createSpecialRestrictedLicence();
        }
        else if (world.createApplication.getApplicationId() == null) {
            world.APIJourney.createApplication();
            world.APIJourney.submitApplication();
        }
    }

    @When("the interim fee has been paid")
    public void theInterimFeeHasBeenPaid() {
        world.grantApplication.getOutstandingFees();
        world.grantApplication.payOutstandingFees();
    }

    @And("the application has been refused")
    public void theApplicationHasBeenRefused() {
        world.grantApplication.refuse(world.createApplication.getApplicationId());
    }

    @Then("the interim fee should be refunded")
    public void theInterimFeeShouldBeRefunded() {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        clickByLinkText("Fees");
        selectValueFromDropDown("//*[@id='status']", SelectorType.XPATH, "All");
        waitForTextToBePresent("£68.00");
        clickByLinkText("Grant Interim Fee for application");
        waitForTextToBePresent("Fee details");
        waitAndClick("refund", SelectorType.ID);
        waitForTextToBePresent("All payments will be refunded");
        world.UIJourney.clickSubmit();
        long kickoutTime = System.currentTimeMillis() + 30000;
        do {
            refreshPageWithJavascript();
        } while(!getText("//*//dd//span", SelectorType.XPATH).toLowerCase().contains("refunded") && System.currentTimeMillis() < kickoutTime);
        if (System.currentTimeMillis() > kickoutTime) {
            throw new TimeoutException("Kickout time for expecting the interim fee to be refunded.");
        }
        assertTrue(getText("//*//dd//span", SelectorType.XPATH).toLowerCase().contains("refunded"));
        assertTrue(checkForPartialMatch("£68.00"));
    }

    @And("the application has been withdrawn")
    public void theApplicationHasBeenWithdrawn() {
        world.grantApplication.withdraw(world.createApplication.getApplicationId());

    }

    @And("the licence is granted")
    public void theLicenceIsGranted() {
        world.APIJourney.grantLicenceAndPayFees();
    }

    @And("the interim is granted")
    public void theInterimIsGranted() {
        world.updateLicence.grantInterimApplication(world.createApplication.getApplicationId());
    }

    @Then("the interim fee should not be refunded")
    public void theInterimFeeShouldNotBeRefunded() {
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
    public void theVariationApplicationHasBeenRefused() {
        world.grantApplication.refuse(world.updateLicence.getVariationApplicationId());
    }

    @And("the variation application has been withdrawn")
    public void theVariationApplicationHasBeenWithdrawn() {
        world.grantApplication.withdraw(world.updateLicence.getVariationApplicationId());
    }

    @And("the variation interim is granted")
    public void theVariationInterimIsGranted() {
        world.updateLicence.grantInterimApplication(world.updateLicence.getVariationApplicationId());
    }
}
