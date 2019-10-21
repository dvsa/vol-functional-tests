package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.Scenario;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;

import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VerifySwitchedOff extends BasePage implements En {

    public VerifySwitchedOff(World world) {
        Given("^i have a \"([^\"]*)\" \"([^\"]*)\" partial application$", (String operatorType, String country) -> {
            world.createLicence.setOperatorType(operatorType);
            if (country.equals("NI")) {
                world.APIJourneySteps.nIAddressBuilder();
            }
            world.APIJourneySteps.registerAndGetUserDetails("selfserve");
            world.APIJourneySteps.createPartialApplication();
        });
        Then("^Signing options are not displayed on the page$", () -> {
            assertFalse(isElementPresent("//*[@type='radio']", SelectorType.XPATH));
            assertFalse(isTextPresent("How would you like to sign the declaration?", 20));
        });
        And("^submit to operator button is displayed$", () -> {
            String buttonName = findElement("form-actions[submit]", SelectorType.ID, 10).getText();
            assertEquals("Submit to operator", buttonName);
        });
        And("^submit to operator button is not displayed$", () -> {
            String buttonName = findElement("form-actions[submit]", SelectorType.ID, 10).getText();
            assertEquals("Submit", buttonName);
        });
        And("^i add a transport manager$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            world.UIJourneySteps.nominateOperatorUserAsTransportManager(1, true);
        });
        When("^the transport manager is the owner$", () -> {
            world.UIJourneySteps.updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
        });
        And("^the transport manager is not the owner$", () -> {
            world.UIJourneySteps.updateTMDetailsAndNavigateToDeclarationsPage("N", "N", "N", "N", "N");
        });
        When("^i submit the application$", () -> {
            click("form-actions[submit]", SelectorType.ID);
        });
        Then("^the print and sign page is displayed$", () -> {
            Assert.assertTrue(isTextPresent("Transport Manager details approved", 10));
            Assert.assertTrue(isTextPresent("Print, sign and return", 10));
        });
        And("^the application status is \"([^\"]*)\"$", (String arg0) -> {
            clickByLinkText("Back to Transport");
            waitForTextToBePresent("Transport Managers");
            Assert.assertTrue(isTextPresent(arg0, 10));
        });
        Then("^the 'Awaiting operator review' post signature page is displayed$", () -> {
            waitForTextToBePresent("What happens next?");
            assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
            assertTrue(isTextPresent("Awaiting operator review", 10));
            assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s",getCurrentDate("d MMM yyyy")),20));
        });
        When("^i am on the the TM landing page$", () -> {
            world.UIJourneySteps.submitTMApplicationAndNavigateToTMLandingPage();
        });
        Then("^a success message banner should be displayed$", () -> {
            Assert.assertTrue(isTextPresent("The user account has been created and form has been emailed to the transport manager", 10));
        });
        After(new String[]{"@SS-Verify-Off"}, (Scenario scenario) -> {
            if(scenario.isFailed() || !scenario.isFailed())
            world.updateLicence.enableDisableVerify("0");
        });
        And("^i navigate to the declarations page$", () -> {
            world.UIJourneySteps.updateTMDetailsAndNavigateToDeclarationsPage("N", "N", "N", "N", "N");
        });
        Given("^verify has been switched \"([^\"]*)\"$", (String arg0) -> {
          if (arg0.toLowerCase().equals("on")){
              world.updateLicence.enableDisableVerify("0");
          } else {
              world.updateLicence.enableDisableVerify("1");
          }
        });
        Then("^the 'Awaiting operator review' verify off page is displayed$", () -> {
            assertTrue(isTextPresent("Awaiting operator review", 10));
        });
    }
}