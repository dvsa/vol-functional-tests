package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import apiCalls.enums.UserType;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;

import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.junit.Assert.*;

public class VerifySwitchedOff extends BasePage implements En {

    public VerifySwitchedOff(World world) {
        Given("^i have a \"([^\"]*)\" \"([^\"]*)\" partial application$", (String operatorType, String country) -> {
            world.createApplication.setOperatorType(operatorType);
            if (country.equals("NI")) {
                world.APIJourneySteps.nIAddressBuilder();
            }
            world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.APIJourneySteps.createPartialApplication();
        });
        And("^transport manager details approved banner appears$", () -> {
            assertTrue(isTextPresent("Transport Manager details approved", 10));
            clickByLinkText("Back to Transport Managers");
        });
        And("^transport manager status is \"([^\"]*)\" and \"([^\"]*)\"$", (String classString, String Text) -> {
            assertTrue(isElementPresent(String.format("//*[contains(@class,'status %s') and contains(text(),'%s')]", classString, Text), SelectorType.XPATH));
        });
        And("^submit to operator button is displayed$", () -> {
            assertTrue(isElementPresent("//h1[contains(text(),'Awaiting operator review')]", SelectorType.XPATH));
            clickByLinkText("Back to Transport Managers");
        });
        And("^submit to operator button is not displayed$", () -> {
            String buttonName = findElement("form-actions[submit]", SelectorType.ID, 10).getText();
            assertEquals("Submit", buttonName);
        });
        And("^i select a transport manager to add$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("application", "Transport Managers");
            waitForTitleToBePresent("Transport Managers");
            waitAndClick("//*[@id='add']", SelectorType.XPATH);
            waitForTitleToBePresent("Add Transport Manager");
            selectValueFromDropDown("data[registeredUser]", SelectorType.ID, String.format("%s %s", world.registerUser.getForeName(), world.registerUser.getFamilyName()));
            click("//*[@id='form-actions[continue]']", SelectorType.XPATH);

        });
        When("^the transport manager is the owner$", () -> {
            world.TMJourneySteps.updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
        });
        And("^the transport manager is not the owner$", () -> {
            world.TMJourneySteps.updateTMDetailsAndNavigateToDeclarationsPage("N", "N", "N", "N", "N");
        });
        When("^i submit the application$", () -> {
            click("form-actions[submit]", SelectorType.ID);
        });
        Then("^the print and sign page is displayed$", () -> {
            Assert.assertTrue(isTextPresent("Transport Manager details approved",30));
            Assert.assertTrue(isTextPresent("Print, sign and return",30));
        });
        Then("^the 'Awaiting operator review' post signature page is displayed$", () -> {
            waitForTextToBePresent("What happens next?");
            assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
            assertTrue(isTextPresent("Awaiting operator review",30));
            assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("d MMM yyyy")),30));
        });
        When("^i am on the the TM landing page$", () -> {
            world.TMJourneySteps.submitTMApplicationAndNavigateToTMLandingPage();
        });
        Then("^a success message banner should be displayed$", () -> {
            Assert.assertTrue(isTextPresent("The user account has been created and form has been emailed to the transport manager",30));
        });
        And("^i navigate to the declarations page$", () -> {
            world.TMJourneySteps.updateTMDetailsAndNavigateToDeclarationsPage("N", "N", "N", "N", "N");
        });
//        Given("^verify has been switched \"([^\"]*)\"$", (String arg0) -> {
//            if (arg0.toLowerCase().equals("on")){
//              world.updateLicence.enableDisableVerify("0");
//          } else {
//              world.updateLicence.enableDisableVerify("1");
//          }
//        });
        Then("^the 'Awaiting operator review' verify off page is displayed$", () -> {
            assertTrue(isTextPresent("Awaiting operator review",30));
        });
    }
}