package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.UserType;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;



import static org.junit.Assert.*;

public class VerifySwitchedOff extends BasePage implements En {
    private final World world;

    public VerifySwitchedOff(World world) {
        this.world = world;
    }

    @Before
    public void getScenarioName(Scenario scenario){
        System.out.println("Testing Scenario:" + scenario.getName());
    }
    @And("i have a {string} {string} partial application")
    public void iHaveAPartialApplication(String operatorType, String country) {
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
        assertTrue(isElementPresent(String.format("//*[contains(@class,'status %s') and contains(text(),'%s')]", classString, Text), SelectorType.XPATH));
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
        world.UIJourney.clickContinue();
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
        waitAndClick("//*[contains(text(),'Print')]", SelectorType.XPATH);
        clickById("submitAndPay");
        world.UIJourney.clickPay();
        world.feeAndPaymentJourney.customerPaymentModule();
        waitForTitleToBePresent("Application overview");
    }

    @Then("the print and sign page is displayed")
    public void thePrintAndSignPageIsDisplayed() {
        Assert.assertTrue(isTextPresent("Transport Manager details approved"));
        Assert.assertTrue(isTextPresent("Print, sign and return"));
    }

}