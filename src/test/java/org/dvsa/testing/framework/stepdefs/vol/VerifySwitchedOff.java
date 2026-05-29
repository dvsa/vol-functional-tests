package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.UserType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerifySwitchedOff extends BasePage {
    private final World world;


    public VerifySwitchedOff(World world) {
        this.world = world;
    }

    @And("transport manager details approved banner appears")
    public void transportManagerDetailsApprovedBannerAppears() {
        if (isTextPresent("Declaration signed through GOV.UK One Login")) {
            click("//*[contains(text(),'Finish')]", SelectorType.XPATH);
        } else {
            assertTrue(isTextPresent("Transport Manager details approved"));
            waitAndClickByLinkText("Back to Transport Managers");
        }
    }

    @And("transport manager status is {string} and {string}")
    public void transportManagerStatusIs(String classString, String Text) {
        assertTrue(isElementPresent(String.format("//*[@class='govuk-tag govuk-tag--%s']", classString), SelectorType.XPATH));
        String actualText = getText(String.format("//*[@class='govuk-tag govuk-tag--%s']", classString), SelectorType.XPATH);
        if (Text.equalsIgnoreCase("Not yet received")) {
            assertTrue(actualText.equalsIgnoreCase(Text) || actualText.equalsIgnoreCase("Received"),
                    String.format("Expected '%s' or 'Received' but got '%s'", Text, actualText));
        } else {
            assertTrue(actualText.equalsIgnoreCase(Text),
                    String.format("Expected '%s' but got '%s'", Text, actualText));
        }
    }

    @And("submit to operator button is displayed")
    public void submitToOperatorButtonIsDisplayed() {
        waitForTextToBePresent("Awaiting operator review");
        assertTrue(isElementPresent("//h1[contains(text(),'Awaiting operator review')]", SelectorType.XPATH));
        if (isElementPresent("//*[contains(text(),'Finish')]", SelectorType.XPATH)) {
            click("//*[contains(text(),'Finish')]", SelectorType.XPATH);
        } else {
            waitAndClickByLinkText("Back to Transport Managers");
        }
    }

    @And("i select a transport manager to add")
    public void iSelectATransportManagerToAdd() {
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.TRANSPORT_MANAGERS);
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTitleToBePresent("Add Transport Manager");
        selectValueFromDropDown("data[registeredUser]", SelectorType.ID, String.format("%s %s", world.registerUser.getForeName(), world.registerUser.getFamilyName()));
        UniversalActions.clickContinue();
    }

    @When("the transport manager is the owner")
    public void theTransportManagerIsTheOwner() {
        world.TMJourney.updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
    }

    @And("the transport manager is not the owner")
    public void theTransportManagerIsNotTheOwner() {
        world.TMJourney.updateTMDetailsAndNavigateToDeclarationsPage("N", "N", "N", "N", "N");
    }

    @When("i submit and pay for the application")
    public void iSubmitAndPayForTheApplication() {
        world.submitApplicationJourney.submitAndPayForApplication();
    }

    @Then("the print and sign page is displayed")
    public void thePrintAndSignPageIsDisplayed() {
        assertTrue(isTextPresent("Transport Manager details approved") || isTextPresent("Declaration signed through GOV.UK One Login"));
        assertTrue(isTextPresent("Print, sign and return") || isTextPresent("Finish"));
    }
}