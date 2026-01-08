package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.VehicleType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static activesupport.driver.Browser.navigate;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.returnNthNumberSequenceInString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrafficAreas extends BasePage {

    World world;

    public TrafficAreas(World world) {
        this.world = world;
    }

    String knownRealNIPostcode = "BT1 1DN";
    String knownRealNorthWestPostcode = "M11 1DF";

    String correspondenceAddressSearchField = "//*[@name='correspondence_address[searchPostcode][postcode]']";
    String correspondenceAddressSearchButton = "//*[@name='correspondence_address[searchPostcode][search]']";
    String correspondenceAddressSearchDropdown = "//*[@name='correspondence_address[searchPostcode][addresses]']";

    String correspondenceAddressLine1 = "//*[@name='correspondence_address[addressLine1]']";
    String correspondenceAddressTown = "//*[@name='correspondence_address[town]']";
    String correspondenceAddressPostcode = "//*[@name='correspondence_address[postcode]']";

    String establishmentAddressSearchField = "//*[@name='establishment_address[searchPostcode][postcode]']";
    String establishmentAddressSearchButton = "//*[@name='establishment_address[searchPostcode][search]']";
    String establishmentAddressSearchDropdown = "//*[@name='establishment_address[searchPostcode][addresses]']";

    String establishmentAddressLine1 = "//*[@name='establishment_address[addressLine1]']";
    String establishmentAddressLine2 = "//*[@name='establishment_address[addressLine2]']";
    String establishmentAddressLine3 = "//*[@name='establishment_address[addressLine3]']";
    String establishmentAddressLine4 = "//*[@name='establishment_address[addressLine4]']";
    String establishmentAddressTown = "//*[@name='establishment_address[town]']";
    String establishmentAddressPostcode = "//*[@name='establishment_address[postcode]']";

    String alertElement = "//p[@role='alert']";
    String NIAddressForGBLicenceErrorMessage = "You have applied for a GB licence, but the address entered falls within the jurisdiction of Northern Ireland";

    @When("i enter and save a NI correspondence address with no establishment address")
    public void iEnterAndSaveANICorrespondenceAddressWithNoEstablishmentAddress() {
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.ADDRESSES);
        enterAndSelectCorrespondenceAddressWithPostcodeSearch(knownRealNIPostcode);
        clearEstablishmentAddress();
        UniversalActions.clickSaveAndContinue();
    }

    @Then("an error explaining that a NI address has been assigned to a GB application is displayed")
    public void anErrorExplainingThatANIAddressHasBeenAssignedToAGBApplicationIsDisplayed() {
        assertTrue(isElementPresent(alertElement, SelectorType.XPATH));
        assertTrue(isTextPresent(NIAddressForGBLicenceErrorMessage));
    }

    @Then("an error explaining that a NI address has been assigned to a GB application is not displayed")
    public void anErrorExplainingThatANIAddressHasBeenAssignedToAGBApplicationIsNotDisplayed() {
        assertFalse(isElementPresent(alertElement, SelectorType.XPATH));
        assertFalse(isTextPresent(NIAddressForGBLicenceErrorMessage));
    }

    @When("i enter and save a NI establishment address")
    public void iEnterAndSaveANIEstablishmentAddress() {
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.ADDRESSES);
        enterAndSelectEstablishmentAddressWithPostcodeSearch(knownRealNIPostcode);
        UniversalActions.clickSaveAndContinue();
    }

    @When("i apply for a new {string} lgv only application and enter a postcode in the North West of England")
    public void iApplyForANewGoodsStandardInternationalApplicationAndEnterAPostcodeInTheNorthWestOfEngland(String GBOrNI) {
        waitAndClickByLinkText("Apply for a new licence");
        world.typeOfLicenceJourney.chooseGBOrNI(GBOrNI);
        clickByXPath("//input[@value='lcat_gv']");
        clickByXPath("//input[@value='ltyp_si']");
        clickByXPath("//input[@value='app_veh_type_lgv']");
        clickByXPath("//*[@id='lgv-declaration-confirmation']");
        UniversalActions.clickSaveAndContinue();
        waitForTitleToBePresent("Apply for a new licence");
        world.createApplication.setApplicationId(returnNthNumberSequenceInString(navigate().getCurrentUrl(), 1));
        world.createApplication.setVehicleType(VehicleType.LGV_ONLY_FLEET.asString());
        waitAndClickByLinkText("Business type");
        UniversalActions.clickSaveAndContinue();
        UniversalActions.clickSaveAndContinue();
        waitAndClickByLinkText("Enter the address yourself");
        waitAndEnterText("addressLine1", SelectorType.ID,"1 Test Street");
        waitAndEnterText("addressTown", SelectorType.ID, "Nottingham");
        waitAndEnterText("postcode", SelectorType.ID, "M11 1DF");
        waitAndEnterText("phone_primary", SelectorType.ID, "01158410220");
        waitAndEnterText("email", SelectorType.ID, world.DataGenerator.getOperatorUserEmail());
        UniversalActions.clickSaveAndContinue();
    }

    @Then("the traffic area is set as North West of England")
    public void theTrafficAreaIsSetAsNorthWestOfEngland() {
        navigateToAppropriateAuthorisationPageAndAssertTextPresent("North West of England");
    }

    @Then("the traffic area is set as Northern Ireland")
    public void theTrafficAreaIsSetAsNorthernIreland() {
        navigateToAppropriateAuthorisationPageAndAssertTextPresent("Northern Ireland");
    }

    @Then("the traffic area is not set as Northern Ireland")
    public void theTrafficAreaIsNotSetAsNorthernIreland() {
        navigateToAppropriateAuthorisationPageAndAssertTextNotPresent("Northern Ireland");
    }

    @Then("the traffic area is not set as North West of England")
    public void theTrafficAreaIsNotSetAsNorthWestOfEngland() {
        navigateToAppropriateAuthorisationPageAndAssertTextNotPresent("Northern Ireland");
    }

    @And("the traffic area dropdown is available")
    public void theTrafficAreaDropdownIsAvailable() {
        assertTrue(isElementPresent("//select[@id='trafficArea']", SelectorType.XPATH));
    }

    @And("the traffic area dropdown is not available")
    public void theTrafficAreaDropdownIsNotAvailable() {
        assertFalse(isElementPresent("//select[@id='trafficArea']", SelectorType.XPATH));
    }

    @And("a licence already exists in this traffic area error appears when I complete the page")
    public void aLicenceAlreadyExistsInThisTrafficAreaErrorAppearsWhenICompleteThePage() {
        enterText("totAuthLgvVehicles", SelectorType.ID, "5");
        enterText("totCommunityLicences", SelectorType.ID, "5");
        selectValueFromDropDown("trafficArea", SelectorType.ID, "North West of England");
        UniversalActions.clickSaveAndContinue();
        waitForUrlToContain("/operating-centres/", 2);
        waitForElementToBePresent("//p[@role='alert']");
        assertTrue(isTextPresent("You already have a Goods application or licence within the North West of England traffic area"));
    }

    @And("a licence already exists in this traffic area error does not appear when I complete the page")
    public void aLicenceAlreadyExistsInThisTrafficAreaErrorDoesNotAppearWhenICompleteThePage() {
        enterText("totAuthLgvVehicles", SelectorType.ID, "5");
        enterText("totCommunityLicences", SelectorType.ID, "5");
        UniversalActions.clickSaveAndContinue();
        assertFalse(isElementPresent("//p[@role='alert']", SelectorType.XPATH));
        assertFalse(isTextPresent("You already have a Goods application or licence within the North West of England traffic area"));
    }

    @When("i complete the application to and enter and save a north west correspondence address with no establishment address")
    public void iCompleteTheApplicationToAndEnterAndSaveANorthWestCorrespondenceAddressWithNoEstablishmentAddress() {
        completeApplicationUntilAddressesPage();
        enterAndSelectCorrespondenceAddressWithPostcodeSearch(knownRealNorthWestPostcode);
        enterPhoneAndEmail();
        UniversalActions.clickSaveAndContinue();
    }

    @When("i complete the application to and enter and save a north west correspondence address and an establishment address")
    public void iCompleteTheApplicationToAndEnterAndSaveANorthWestCorrespondenceAddressAndAnEstablishmentAddress() {
        completeApplicationUntilAddressesPage();
        enterAndSelectCorrespondenceAddressWithPostcodeSearch(knownRealNorthWestPostcode);
        enterPhoneAndEmail();
        enterAndSelectEstablishmentAddressWithPostcodeSearch(knownRealNorthWestPostcode);
        UniversalActions.clickSaveAndContinue();
    }

    @When("i complete the application to and enter and save a north west correspondence address and a non-uk establishment address")
    public void iCompleteTheApplicationToAndEnterAndSaveANorthWestCorrespondenceAddressAndANonUkEstablishmentAddress() {
        completeApplicationUntilAddressesPage();
        enterAndSelectCorrespondenceAddressWithPostcodeSearch(knownRealNorthWestPostcode);
        enterPhoneAndEmail();
        waitAndClickByLinkText("Enter the address yourself");
        waitForElementToBePresent(establishmentAddressPostcode);
        waitAndClick(establishmentAddressPostcode, SelectorType.XPATH);
        enterText(establishmentAddressLine1, SelectorType.XPATH, "test");
        enterText(establishmentAddressTown, SelectorType.XPATH, "test");
        enterText(establishmentAddressPostcode, SelectorType.XPATH, "FG67FG");
        UniversalActions.clickSaveAndContinue();
    }


    @When("i complete the application to and enter and save a non-uk correspondence address")
    public void iCompleteTheApplicationToAndEnterAndSaveANonUkCorrespondenceAddress() {
        completeApplicationUntilAddressesPage();
        waitAndClickByLinkText("Enter the address yourself");
        waitForElementToBePresent(correspondenceAddressLine1);
        enterText(correspondenceAddressLine1, SelectorType.XPATH, "test");
        enterText(correspondenceAddressTown, SelectorType.XPATH, "test");
        enterText(correspondenceAddressPostcode, SelectorType.XPATH, "FG67FG");
        enterPhoneAndEmail();
        UniversalActions.clickSaveAndContinue();
    }

    @And("I add an operating centre with a northern ireland postcode")
    public void iAddAnOperatingCentreWithANorthernIrelandPostcode() {
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.OPERATING_CENTERS_AND_AUTHORISATION);
        world.createApplication.setPostCodeByTrafficArea(knownRealNIPostcode);
        world.operatingCentreJourney.addNewOperatingCentre("5", "5");
    }

    private void completeApplicationUntilAddressesPage() {
        waitAndClickByLinkText("Business type");
        UniversalActions.clickSaveAndContinue();
        enterText("//*[@id='data[companyNumber][company_number]']", SelectorType.XPATH, "12321212");
        click("//*[@id='data[companyNumber][submit_lookup_company]']", SelectorType.XPATH);
        enterText("//*[@id='natureOfBusiness']", SelectorType.XPATH, "test nature of business");
        UniversalActions.clickSaveAndContinue();
    }

    private void enterAndSelectCorrespondenceAddressWithPostcodeSearch(String postcode) {
        waitAndEnterText(correspondenceAddressSearchField, SelectorType.XPATH, postcode);
        waitAndClick(correspondenceAddressSearchButton, SelectorType.XPATH);
        waitForElementToBePresent(correspondenceAddressSearchDropdown);
        selectValueFromDropDownByIndex(correspondenceAddressSearchDropdown, SelectorType.XPATH, 1);
        waitForElementToBeClickable(correspondenceAddressLine1, SelectorType.XPATH);
    }

    private void enterAndSelectEstablishmentAddressWithPostcodeSearch(String postcode) {
        enterText(establishmentAddressSearchField, SelectorType.XPATH, postcode);
        click(establishmentAddressSearchButton, SelectorType.XPATH);
        waitForElementToBePresent(establishmentAddressSearchDropdown);
        selectValueFromDropDownByIndex(establishmentAddressSearchDropdown, SelectorType.XPATH, 1);
        waitForElementToBeClickable(establishmentAddressLine1, SelectorType.XPATH);
    }

    private void clearEstablishmentAddress() {
        waitAndClick(establishmentAddressPostcode, SelectorType.XPATH);
        replaceText(establishmentAddressLine1, SelectorType.XPATH, "");
        replaceText(establishmentAddressLine2, SelectorType.XPATH, "");
        replaceText(establishmentAddressLine3, SelectorType.XPATH, "");
        replaceText(establishmentAddressLine4, SelectorType.XPATH, "");
        replaceText(establishmentAddressTown, SelectorType.XPATH, "");
        replaceText(establishmentAddressPostcode, SelectorType.XPATH, "");
    }

    private void enterPhoneAndEmail() {
        waitAndClick("//input[@id='phone_primary']", SelectorType.XPATH);
        enterText("phone_primary", SelectorType.ID, "01827353");
        enterText("email", SelectorType.ID, "test@test.com");
    }

    private void navigateToAppropriateAuthorisationPageAndAssertTextPresent(String trafficAreaText) {
        navigateToAppropriateAuthorisationPageAndAssert(trafficAreaText, true);
    }

    private void navigateToAppropriateAuthorisationPageAndAssertTextNotPresent(String trafficAreaText) {
        navigateToAppropriateAuthorisationPageAndAssert(trafficAreaText, false);
    }

    private void navigateToAppropriateAuthorisationPageAndAssert(String trafficAreaText, boolean presence) {
        if (world.createApplication.getVehicleType().equals(VehicleType.LGV_ONLY_FLEET.asString())) {
              UniversalActions.clickHome();
            world.selfServeNavigation.navigateToPage("application", SelfServeSection.LICENCE_AUTHORISATION);
        } else {
              UniversalActions.clickHome();
            world.selfServeNavigation.navigateToPage("application", SelfServeSection.OPERATING_CENTERS_AND_AUTHORISATION);
        }
        if (presence) {
            assertTrue(isTextPresent(trafficAreaText));
        } else
            assertFalse(isTextPresent(trafficAreaText));
    }
}
