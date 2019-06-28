package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.APIJourneySteps;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.Optional;

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
            world.APIJourneySteps.registerAndGetUserDetails();
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
            world.updateLicence.createInternalAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.getAdminUserLogin(), world.updateLicence.getAdminUserEmailAddress());
            world.UIJourneySteps.searchAndViewLicence();
            clickByLinkText("Fees");
            do {
                waitAndClick("//*[@id=\"status\"]/option[@value='all']", SelectorType.XPATH);
            } while (!isTextPresent("Paid",10));
            assertTrue(checkForPartialMatch("£68.00"));
            assertTrue(world.genericUtils.returnFeeStatus("CANCELLED"));
        });
        And("^the licence has been withdrawn$", () -> {
            world.grantLicence.withdraw(world.createLicence.getApplicationNumber());
        });
        Then("^the interim fee should not be refunded$", () -> {
            world.updateLicence.createInternalAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.getAdminUserLogin(), world.updateLicence.getAdminUserEmailAddress());
            world.UIJourneySteps.urlSearchAndViewLicence();
            clickByLinkText("Fees");
            do {
                waitAndClick("//*[@id=\"status\"]/option[@value='all']", SelectorType.XPATH);
            } while (!isTextPresent("Paid",10));
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
        });
        And("^the application has been refused$", () -> {
            world.grantLicence.refuse(String.valueOf(Integer.parseInt(world.createLicence.getApplicationNumber()) + 1));
        });
        And("^the application has been withdrawn$", () -> {
            world.grantLicence.withdraw(String.valueOf(Integer.parseInt(world.createLicence.getApplicationNumber()) + 1));
        });
        And("^the variation interim is granted$", () -> {
            world.updateLicence.grantInterimApplication(String.valueOf(Integer.parseInt(world.createLicence.getApplicationNumber()) + 1));
        });
    }
}