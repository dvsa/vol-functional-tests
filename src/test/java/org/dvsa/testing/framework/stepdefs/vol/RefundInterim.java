package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.UserRoles;
import apiCalls.enums.UserType;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.TimeoutException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RefundInterim extends BasePage implements En {
    public RefundInterim(World world) {
        Given("^i have an interim \"([^\"]*)\" \"([^\"]*)\" application$", (String operatorType, String licenceType) -> {
            if (operatorType.equals("public")){
                throw new Exception("PSV licences cannot have interim applications.");
            }
            world.createApplication.setOperatorType(operatorType);
            world.createApplication.setLicenceType(licenceType);
            world.createApplication.setIsInterim("Y");
            world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            if(licenceType.equals("special_restricted") && (world.createApplication.getApplicationId() == null)){
                world.APIJourneySteps.createSpecialRestrictedLicence();
            }
            else if (world.createApplication.getApplicationId() == null) {
                world.APIJourneySteps.createApplication();
                world.APIJourneySteps.submitApplication();
            }
        });
        When("^the interim fee has been paid$", () -> {
            world.grantApplication.getOutstandingFees();
            world.grantApplication.payOutstandingFees();
        });
        And("^the application has been refused$", () -> {
            world.grantApplication.refuse(world.createApplication.getApplicationId());
        });
        Then("^the interim fee should be refunded$", () -> {
            world.updateLicence.createInternalUser(UserRoles.INTERNAL_ADMIN.asString(),UserType.INTERNAL.asString());
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalNavigation.urlSearchAndViewLicence();
            clickByLinkText("Fees");
            selectValueFromDropDown("//*[@id='status']", SelectorType.XPATH, "All");
            waitForTextToBePresent("£68.00");
            clickByLinkText("Grant Interim Fee for application");
            waitForTextToBePresent("Fee details");
            long kickoutTime = System.currentTimeMillis() + 30000;
            do {
                javaScriptExecutor("location.reload(true)");
            } while(!getText("//*//dd//span", SelectorType.XPATH).toLowerCase().contains("refunded") && System.currentTimeMillis() < kickoutTime);
            if (System.currentTimeMillis() > kickoutTime) {
                throw new TimeoutException("Kickout time for expecting the interim fee to be refunded.");
            }
            assertTrue(getText("//*//dd//span", SelectorType.XPATH).toLowerCase().contains("refunded"));
            assertTrue(checkForPartialMatch("£68.00"));
        });
        And("^the application has been withdrawn$", () -> {
            world.grantApplication.withdraw(world.createApplication.getApplicationId());
        });
        Then("^the interim fee should not be refunded$", () -> {
            world.updateLicence.createInternalUser(UserRoles.INTERNAL_ADMIN.asString(),UserType.INTERNAL.asString());
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalNavigation.urlSearchAndViewLicence();
            clickByLinkText("Fees");
            do {
                waitAndClick("//*[@id=\"status\"]/option[@value='all']", SelectorType.XPATH);
            } while (!isTextPresent("Paid",30));
            assertTrue(checkForPartialMatch("£68.00"));
            assertFalse(world.genericUtils.returnFeeStatus("CANCELLED"));
        });
        And("^the licence is granted$", () -> {
            world.APIJourneySteps.grantLicenceAndPayFees();
        });
        And("^the interim is granted$", () -> {
            world.updateLicence.grantInterimApplication(world.createApplication.getApplicationId());
        });
        When("^i pay for the interim application$", () -> {
            world.UIJourneySteps.payForInterimApp();
            waitForTitleToBePresent("Application overview");
        });
        And("^the variation application has been refused$", () -> {
            world.grantApplication.refuse(world.updateLicence.getVariationApplicationId());
        });
        And("^the variation application has been withdrawn$", () -> {
            world.grantApplication.withdraw(world.updateLicence.getVariationApplicationId());
        });
        And("^the variation interim is granted$", () -> {
            world.updateLicence.grantInterimApplication(world.updateLicence.getVariationApplicationId());
        });
    }
}