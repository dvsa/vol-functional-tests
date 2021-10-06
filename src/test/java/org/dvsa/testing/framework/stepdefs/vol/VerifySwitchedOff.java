package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.faker.FakerUtils;
import apiCalls.enums.UserType;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.Driver.DriverUtils;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static activesupport.driver.Browser.navigate;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.junit.Assert.*;

public class VerifySwitchedOff extends BasePage {
    World world;

    public VerifySwitchedOff(World world) {
        this.world = world;
    }


    @Given("i have a {string} {string} partial application")
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
    public void transportManagerStatusIsAnd(String classString, String Text) {
        assertTrue(isElementPresent(String.format("//*[contains(@class,'status %s') and contains(text(),'%s')]", classString, Text), SelectorType.XPATH));
    }

    @And("submit to operator button is displayed")
    public void submitToOperatorButtonIsDisplayed() {
        assertTrue(isElementPresent("//h1[contains(text(),'Awaiting operator review')]", SelectorType.XPATH));
        clickByLinkText("Back to Transport Managers");
    }

    @And("submit to operator button is not displayed")
    public void submitToOperatorButtonIsNotDisplayed() {
        String buttonName = findElement("form-actions[submit]", SelectorType.ID, 10).getText();
        assertEquals("Submit", buttonName);
    }

    @And("i select a transport manager to add")
    public void iSelectATransportManagerToAdd() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("application", "Transport Managers");
        waitForTitleToBePresent("Transport Managers");
        waitAndClick("//*[@id='add']", SelectorType.XPATH);
        waitForTitleToBePresent("Add Transport Manager");
        selectValueFromDropDown("data[registeredUser]", SelectorType.ID, String.format("%s %s", world.registerUser.getForeName(), world.registerUser.getFamilyName()));
        click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
    }

    @When("the transport manager is the owner")
    public void theTransportManagerIsTheOwner() {
        world.TMJourney.updateTMDetailsAndNavigateToDeclarationsPage("Y", "N", "N", "N", "N");
    }

    @And("the transport manager is not the owner")
    public void theTransportManagerIsNotTheOwner() {
        world.TMJourney.updateTMDetailsAndNavigateToDeclarationsPage("N", "N", "N", "N", "N");
    }

    @When("i submit the application")
    public void iSubmitTheApplication() {
        click("form-actions[submit]", SelectorType.ID);
    }

    @Then("the print and sign page is displayed")
    public void thePrintAndSignPageIsDisplayed() {
        Assert.assertTrue(isTextPresent("Transport Manager details approved"));
        Assert.assertTrue(isTextPresent("Print, sign and return"));
    }

    @Then("the 'Awaiting operator review' post signature page is displayed")
    public void theAwaitingOperatorReviewPostSignaturePageIsDisplayed() {
        waitForTextToBePresent("What happens next?");
        assertTrue(isElementPresent("//*[@class='govuk-panel govuk-panel--confirmation']", SelectorType.XPATH));
        assertTrue(isTextPresent("Awaiting operator review"));
        assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("d MMM yyyy"))));
    }

    @When("i am on the the TM landing page")
    public void iamOnTheTheTMLandingPage() {
        world.TMJourney.submitTMApplicationAndNavigateToTMLandingPage();
    }

    @Then("a success message banner should be displayed")
    public void aSuccessMessageBannerShouldBeDisplayed() {
        Assert.assertTrue(isTextPresent("The user account has been created and form has been emailed to the transport manager"));
    }

    @And("i navigate to the declarations page")
    public void iNavigateToTheDeclarationsPage() {
        world.TMJourney.updateTMDetailsAndNavigateToDeclarationsPage("N", "N", "N", "N", "N");
    }

    @Then("the 'Awaiting operator review' verify off page is displayed")
    public void theAwaitingOperatorReviewVerifyOffPageIsDisplayed() {
        assertTrue(isTextPresent("Awaiting operator review"));
    }

    @Given("i start a new licence application")
    public void iStartANewLicenceApplication() {
        //Move to UIJourney
        FakerUtils faker = new FakerUtils();
        String workingDir = System.getProperty("user.dir");
        String financialEvidenceFile = "/src/test/resources/newspaperAdvert.jpeg";
        String myURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();

        DriverUtils.get(myURL);
        world.globalMethods.signIn("jacobfinney",);
        clickByXPath("//*[contains(text(),'Apply for a new licence')]");
        clickByXPath("//*[contains(text(),'Goods')]");
        clickByXPath("//*[contains(text(),'Standard National')]");
        clickByXPath("//*[contains(text(),'Save')]");

        clickByXPath("//*[contains(text(),'Business type')]");
        String saveAndContinue = "//*[@id='form-actions[saveAndContinue]']";
        waitAndClick(saveAndContinue, SelectorType.XPATH);

        waitForTitleToBePresent("Business details");
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Addresses");
        waitAndEnterText("correspondence_address[searchPostcode][postcode]",SelectorType.NAME,"NG1 6LP");
        clickByName("correspondence_address[searchPostcode][search]");
        waitAndSelectByIndex("Select an address","//*[@id='selectAddress1']",SelectorType.XPATH, 1);
        waitAndEnterText("contact[phone_primary]",SelectorType.NAME,"07123456780");
        waitAndEnterText("contact[email]",SelectorType.NAME,faker.bothify("????????##@volTest.org"));

        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Responsible people");

        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitForTitleToBePresent("Operating centres and authorisation");
        clickByXPath("//*[contains(text(),'Add operating centre')]");
        waitAndEnterText("address[searchPostcode][postcode]",SelectorType.NAME,"B44 9UL");
        clickByName("address[searchPostcode][search]");
        waitAndSelectByIndex("Select an address","//*[@id='selectAddress1']",SelectorType.XPATH, 1);
        waitAndEnterText("data[noOfVehiclesRequired]",SelectorType.NAME,"6");
        waitAndEnterText("data[noOfTrailersRequired]",SelectorType.NAME,"6");
        clickById("permission");
        waitAndClick("//*[contains(text(),'Upload documents now')]",SelectorType.XPATH);

        uploadFile("//*[@id='advertisements[adPlacedContent][file][file]']", workingDir + financialEvidenceFile, "document.getElementById('advertisements[adPlacedContent][file][file]').style.left = 0", SelectorType.XPATH);

        waitAndEnterText("adPlacedIn",SelectorType.ID, "VOL Tribune");
        waitAndEnterText("adPlacedDate_day",SelectorType.ID, String.valueOf(LocalDate.now().getDayOfMonth()));
        waitAndEnterText("adPlacedDate_month",SelectorType.ID, String.valueOf(LocalDate.now().getMonthValue()));
        waitAndEnterText("adPlacedDate_year",SelectorType.ID, String.valueOf(LocalDate.now().getYear()));
        waitAndClick("form-actions[submit]", SelectorType.NAME);
        waitAndClick(saveAndContinue, SelectorType.XPATH);
        waitAndEnterText("data[totAuthVehicles]",SelectorType.NAME,"6");
        waitAndEnterText("data[totAuthTrailers]",SelectorType.NAME,"6");
        waitForTitleToBePresent("Financial evidence");
    }
}