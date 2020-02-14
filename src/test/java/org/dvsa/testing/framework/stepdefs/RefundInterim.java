package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import enums.UserRoles;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.TimeoutException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RefundInterim extends BasePage implements En {
    public RefundInterim(World world) {
        Given("^i have an interim \"([^\"]*)\" \"([^\"]*)\" application$", (String operatorType, String licenceType) -> {
            if (licenceType.equals("si")) {
                world.createLicence.setLicenceType("standard_international");
            } else if (licenceType.equals("sn")) {
                world.createLicence.setLicenceType("standard_national");
            } else {
                world.createLicence.setLicenceType("standard_national");
            }
            world.createLicence.setIsInterim("Y");
            world.createLicence.setOperatorType(operatorType);
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.APIJourneySteps.createApplication();
            world.APIJourneySteps.submitApplication();
        });
        When("^the interim fee has been paid$", () -> {
            world.grantLicence.getOutstandingFees(world.createLicence.getApplicationNumber());
            world.grantLicence.payOutstandingFees(world.createLicence.getOrganisationId(), world.createLicence.getApplicationNumber());
        });
        And("^the licence has been refused$", () -> {
            world.grantLicence.refuse(world.createLicence.getApplicationNumber());
        });
        Then("^the interim fee should be refunded$", () -> {
            world.updateLicence.createInternalUser(UserRoles.INTERNAL_ADMIN.getUserRoles(),UserRoles.INTERNAL.getUserRoles());
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.getAdminUserLogin(), world.updateLicence.getAdminUserEmailAddress());
            world.UIJourneySteps.urlSearchAndViewLicence();
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
        And("^the licence has been withdrawn$", () -> {
            world.grantLicence.withdraw(world.createLicence.getApplicationNumber());
        });
        Then("^the interim fee should not be refunded$", () -> {
            world.updateLicence.createInternalUser(UserRoles.INTERNAL_ADMIN.getUserRoles(),UserRoles.INTERNAL.getUserRoles());
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.getAdminUserLogin(), world.updateLicence.getAdminUserEmailAddress());
            world.UIJourneySteps.urlSearchAndViewLicence();
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
            world.updateLicence.grantInterimApplication(world.createLicence.getApplicationNumber());
        });
        When("^i pay for the interim application$", () -> {
            world.UIJourneySteps.payForInterimApp();
            waitForTextToBePresent("Application overview");
        });
        And("^the application has been refused$", () -> {
            world.grantLicence.refuse(world.updateLicence.getVariationApplicationNumber());
        });
        And("^the application has been withdrawn$", () -> {
            world.grantLicence.withdraw(world.updateLicence.getVariationApplicationNumber());
        });
        And("^the variation interim is granted$", () -> {
            world.updateLicence.grantInterimApplication(world.updateLicence.getVariationApplicationNumber());
        });
    }
}