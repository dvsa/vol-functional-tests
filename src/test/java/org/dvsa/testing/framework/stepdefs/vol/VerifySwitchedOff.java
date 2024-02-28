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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerifySwitchedOff extends BasePage {
    private final World world;
    Initialisation initialisation;

    public VerifySwitchedOff(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @And("i have a {string} {string} partial application")
    public void iHaveAPartialApplication(String operatorType, String country) throws HttpException {
        world.createApplication.setOperatorType(operatorType);
        if (country.equals("NI")) {
            world.APIJourney.nIAddressBuilder();
        }
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.APIJourney.createPartialApplication();
    }

    @And("transport manager details approved banner appears")
    public void transportManagerDetailsApprovedBannerAppears() {
        assertTrue(isTextPresent("Transport Manager details approved"));
        clickByLinkText("Back to Transport Managers");
    }

    @And("transport manager status is {string} and {string}")
    public void transportManagerStatusIs(String classString, String Text) {
        assertTrue(isElementPresent(String.format("//*[@class='govuk-tag govuk-tag--%s']", classString), SelectorType.XPATH));
        assertTrue(getText(String.format("//*[@class='govuk-tag govuk-tag--%s']", classString), SelectorType.XPATH).equals(Text.toUpperCase()));
    }

    @And("submit to operator button is displayed")
    public void submitToOperatorButtonIsDisplayed() {
        assertTrue(isElementPresent("//h1[contains(text(),'Awaiting operator review')]", SelectorType.XPATH));
        clickByLinkText("Back to Transport Managers");
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
        assertTrue(isTextPresent("Transport Manager details approved"));
        assertTrue(isTextPresent("Print, sign and return"));
    }
}