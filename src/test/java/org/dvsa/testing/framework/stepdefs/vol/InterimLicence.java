package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.dates.Dates;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.InterimPage;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.joda.time.LocalDate;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterimLicence extends BasePage {

    private static final String VehicleErrorMessage = "The interim vehicle authority cannot exceed the total vehicle authority";
    private static final String HgvVehicleErrorMessage = "The interim Heavy goods vehicle authority cannot exceed the total Heavy goods vehicle authority";
    private static final String LgvVehicleErrorMessage = "The interim Light goods vehicle authority cannot exceed the total Light goods vehicle authority";
    private static final String errorMessageTitle = "There is a problem";
    private static final String valueIsRequiredErrorMessage = "Value is required";
    private static final String valueCannotBe0ErrorMessage = "A value greater than 0 must be entered";
    private static final String lgvAuthorityLabelText = "Light Goods Vehicle Authority";
    private static final String hgvAuthorityLabelText = "Heavy Goods Vehicle Authority";
    private static final String lgvAuthField = "//*[@id='interimAuthLgvVehicles']";
    private static final String hgvAuthField = "//*[@id='interimAuthHgvVehicles']";
    private static final String trailerAuthField = "//*[@id='interimAuthTrailers']";
    private static final String vehicleAuthLabelElement = "//label[text()='Vehicle Authority']";
    private static final String trailerAuthLabelElement = "//label[text()='Trailer Authority']";
    private static final String interimGrantModalHeading = "Are you sure you want to grant this interim?";
    private static final String interimGrantConfirmation = "The interim has been granted and a fee request letter has been generated";
    private final String interimOfferText = "Do you want to apply for authority to operate whilst your full application is considered?";
    private final String interimRadioYes = "//*[@id='interim[goodsApplicationInterim]']";
    private final World world;

    public InterimLicence(World world) { this.world = world; }

    @When("I have an interim vehicle authority greater than my application vehicle authority")
    public void iHaveAnInterimVehicleAuthorityGreateThanMyApplicationVehicleAuthority() {
        InterimPage.addInterimValues();
        InterimPage.vehicleHgvAuthority(world.createApplication.getNoOfAddedHgvVehicles() + 1);
    }

    @When("I have an interim vehicle authority equal to my application vehicle authority")
    public void iHaveAnInterimVehicleAuthorityEqualToMyApplicationVehicleAuthority(){
        InterimPage.addInterimValues();
        InterimPage.vehicleHgvAuthority(world.createApplication.getNoOfAddedHgvVehicles());
    }

    @When("I have an interim vehicle authority less than my application vehicle authority")
    public void iHaveAnInterimVehicleAuthorityLessThanMyApplicationVehicleAuthority () {
        InterimPage.addInterimValues();
        InterimPage.vehicleHgvAuthority(world.createApplication.getNoOfAddedHgvVehicles() - 1);
    }

    @When("I have an interim vehicle authority with {string} hgvs and {string} lgvs")
    public void iHaveAnInterimVehicleAuthorityWithHGVsAndLGVs(String intHGVs, String intLGVs) {
        InterimPage.addInterimValues();
        InterimPage.vehicleHgvAuthority(Integer.parseInt(intHGVs));
        InterimPage.vehicleLgvAuthority(Integer.parseInt(intLGVs));
    }

    @When("I create an interim application with no start and end dates")
    public void iCreateAnInterimApplicationWithNoStartAndEndDates() {
        waitAndClickByLinkText("add interim");
        findSelectAllRadioButtonsByValue("Y");
        InterimPage.enterInterimDetail("Test Test");
        InterimPage.vehicleHgvAuthority(world.createApplication.getTotalOperatingCentreHgvAuthority());
        InterimPage.trailerAuthority(world.createApplication.getTotalOperatingCentreTrailerAuthority());
    }

    @Then("A {string} error appears when i save the interim licence")
    public void AErrorAppearsWhenISaveTheInterimLicence(String errorType) {
        UniversalActions.clickSaveAndReturn();
        String errorMes = (errorType.equals("HGV") ? (HgvVehicleErrorMessage):(LgvVehicleErrorMessage));
        assertTrue(isTextPresent(errorMes));
    }

    @Then("I should get an error when i save the application")
    public void iShouldGetAnErrorWhenISaveTheApplication() {
        UniversalActions.clickSaveAndReturn();
        assertTrue(isTextPresent(VehicleErrorMessage));
    }

    @Then("I should be able to save the application without any errors")
    public void iShouldBeAbleToSaveTheApplicationWithoutAnyErrors() {
        UniversalActions.clickSaveAndReturn();
        assertFalse(isTextPresent(VehicleErrorMessage));
        assertFalse(isTextPresent(HgvVehicleErrorMessage));
        assertFalse(isTextPresent(LgvVehicleErrorMessage));
    }

    @Then("I should not error when i save the application")
    public void iShouldNotErrorWhenISaveTheApplication() {
        UniversalActions.clickSaveAndReturn();
        assertFalse(isTextPresent(valueIsRequiredErrorMessage));
    }

    @Then("I should error when i attempt to grant the application")
    public void iShouldErrorWhenIAttemptToGrantTheApplication() {
        UniversalActions.clickSaveAndReturn();
        waitAndClickByLinkText("Interim details");
        waitForTextToBePresent("Interim application");
        InterimPage.grant();
        isTextPresent(valueIsRequiredErrorMessage);
    }

    @When("I create an interim application with a start and no end date")
    public void iCreateAnInterimApplicationWithAStartAndNoEndDate() {
        waitAndClickByLinkText("add interim");
        findSelectAllRadioButtonsByValue("Y");
        InterimPage.startDate(10, 8, 2017);
        InterimPage.enterInterimDetail("Interim with no dates");
        InterimPage.vehicleHgvAuthority((world.createApplication.getTotalOperatingCentreHgvAuthority()));
        InterimPage.trailerAuthority(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        // Not sure why this is setting authorities.
    }

    @And("i create a variation in internal")
    public void iCreateAVariationInInternal() {
        world.internalUIJourney.createVariationInInternal(false);
    }

    @And("I submit the application with an interim")
    public void iSubmitTheApplicationWithAnInterim() {
        //world.selfServeNavigation.navigateToLoginPage();
        //world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.REVIEW_AND_DECLARATIONS);
        click("//*[@id='interim[goodsApplicationInterim]']", SelectorType.XPATH);
        enterText("applicationInterimReason", SelectorType.ID, "Sample Text For Interim");
        click("//*[contains(text(),'Print, sign and return')]/../../input", SelectorType.XPATH);
        click("//*[@name='form-actions[submitAndPay]']", SelectorType.XPATH);
        UniversalActions.clickPay();
        world.feeAndPaymentJourney.customerPaymentModule();
        waitAndClick("confirm", SelectorType.ID);
        waitForTitleToBePresent("Application overview");
    }

    @When("i view the application interim on internal")
    public void iViewTheApplicationInterimOnInternal() throws HttpException {
        world.internalNavigation.navigateToPage("application", SelfServeSection.VIEW);
        waitAndClickByLinkText("Interim details");
        waitForTextToBePresent("Interim requested");
    }

    @Then("only the LGV related details are displayed on the interim")
    public void onlyTheLGVRelatedDetailsAreDisplayedOnTheInterim() {
        assertTrue(isTextPresent(lgvAuthorityLabelText));
        assertTrue(isElementPresent(lgvAuthField, SelectorType.XPATH));

        assertFalse(isElementPresent(vehicleAuthLabelElement, SelectorType.XPATH));
        assertFalse(isElementPresent(hgvAuthField, SelectorType.XPATH));
        assertFalse(isElementPresent(trailerAuthLabelElement, SelectorType.XPATH));
        assertFalse(isElementPresent(trailerAuthField, SelectorType.XPATH));
    }

    @When("i try to grant the interim without entering an authority")
    public void iTryToGrantTheInterimWithoutEnteringAnAuthority() {
        enterInterimDatesAsToday();
        click("grant", SelectorType.ID);
    }

    @Then("an interim authority value is required error message should display")
    public void anInterimAuthorityValueIsRequiredErrorMessageShouldDisplay() {
        waitForTextToBePresent(errorMessageTitle);
        assertTrue(isTextPresent(valueIsRequiredErrorMessage));
        String inlineErrorMessage = String.format("//p[@class='govuk-error-message' and contains(text(),'%s')]", valueIsRequiredErrorMessage);
        assertTrue(isElementPresent(inlineErrorMessage, SelectorType.XPATH));
    }


    @When("i enter an interim lgv authority exceeding the amount on the application")
    public void iEnterAnInterimLgvAuthorityExceedingTheAmountOnTheApplication() {
        enterInterimDatesAsToday();
        enterText(lgvAuthField, 500, SelectorType.XPATH);
        click("grant", SelectorType.ID);
    }

    @Then("an interim lgv auth value exceeds application lgv authority value error message should display")
    public void anInterimLgvAuthValueExceedsApplicationLgvAuthorityValueErrorMessageShouldDisplay() {
        waitForTextToBePresent(errorMessageTitle);
        assertTrue(isTextPresent(LgvVehicleErrorMessage));
        String inlineErrorMessage = String.format("//p[@class='govuk-error-message' and contains(text(),'%s')]", LgvVehicleErrorMessage);
        assertTrue(isElementPresent(inlineErrorMessage, SelectorType.XPATH));
    }

    @When("i enter zero in the interim lgv authority")
    public void iEnterZeroInTheInterimLgvAuthority() {
        enterInterimDatesAsToday();
        enterText(lgvAuthField, 0, SelectorType.XPATH);
        click("grant", SelectorType.ID);
    }

    @Then("a zero lgv auth interim error message should display")
    public void aZeroLgvAuthInterimErrorMessageShouldDisplay() {
        waitForTextToBePresent(errorMessageTitle);
        assertTrue(isTextPresent(valueCannotBe0ErrorMessage));
        String inlineErrorMessage = String.format("//p[@class='govuk-error-message' and contains(text(),'%s')]", valueCannotBe0ErrorMessage);
        assertTrue(isElementPresent(inlineErrorMessage, SelectorType.XPATH));
    }

    @Then("hgv, lgv and trailer interim authorities should be present")
    public void hgvLgvAndTrailerInterimAuthoritiesShouldBePresent() {
        assertTrue(isTextPresent(lgvAuthorityLabelText));
        assertTrue(isElementPresent(lgvAuthField, SelectorType.XPATH));

        assertTrue(isTextPresent(hgvAuthorityLabelText));
        assertTrue(isElementPresent(hgvAuthField, SelectorType.XPATH));

        assertTrue(isElementPresent(trailerAuthLabelElement, SelectorType.XPATH));
        assertTrue(isElementPresent(trailerAuthField, SelectorType.XPATH));

        assertFalse(isElementPresent(vehicleAuthLabelElement, SelectorType.XPATH));
    }

    @Then("only vehicle and trailer interim authorisations should be present")
    public void onlyVehicleAndTrailerInterimAuthorisationsShouldBePresent() {
        assertFalse(isTextPresent(lgvAuthorityLabelText));
        assertFalse(isElementPresent(lgvAuthField, SelectorType.XPATH));

        assertFalse(isTextPresent(hgvAuthorityLabelText));

        assertTrue(isElementPresent(vehicleAuthLabelElement, SelectorType.XPATH));
        assertTrue(isElementPresent(hgvAuthField, SelectorType.XPATH));

        assertTrue(isElementPresent(trailerAuthLabelElement, SelectorType.XPATH));
        assertTrue(isElementPresent(trailerAuthField, SelectorType.XPATH));
    }

    @Then("only vehicle interim authorities should display")
    public void onlyVehicleInterimAuthoritiesShouldDisplay() {
        assertFalse(isTextPresent(lgvAuthorityLabelText));
        assertFalse(isElementPresent(lgvAuthField, SelectorType.XPATH));

        assertFalse(isTextPresent(hgvAuthorityLabelText));

        assertTrue(isElementPresent(vehicleAuthLabelElement, SelectorType.XPATH));
        assertTrue(isElementPresent(hgvAuthField, SelectorType.XPATH));

        assertFalse(isElementPresent(trailerAuthLabelElement, SelectorType.XPATH));
        assertFalse(isElementPresent(trailerAuthField, SelectorType.XPATH));
    }

    private void enterInterimDatesAsToday() {
        HashMap<String, String> randomDate = new Dates(LocalDate::new).getDateHashMap(0,0,0);
        enterDateFieldsByPartialId("interimStart", randomDate);
        enterDateFieldsByPartialId("interimEnd", randomDate);
    }

    @And("the lgv mixed interim is granted on internal")
    public void theLgvMixedInterimIsGrantedOnInternal() throws HttpException {
        world.internalNavigation.navigateToPage("application", SelfServeSection.VIEW);
        waitAndClickByLinkText("Interim details");
        waitForTextToBePresent("Interim requested");
        HashMap<String, String> futureDate = new Dates(LocalDate::new).getDateHashMap(1, 0, 0);
        enterDateFieldsByPartialId("interimStart", futureDate);
        enterDateFieldsByPartialId("interimEnd", futureDate);
        enterText("interimAuthLgvVehicles", SelectorType.ID, String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority()));
        click("grant", SelectorType.ID);
        waitForTextToBePresent(interimGrantModalHeading);
        UniversalActions.clickSubmit();
        waitForTextToBePresent(interimGrantConfirmation);
    }

    @Then("i can request an interim on the {string}")
    public void iCanRequestAnInterim(String applicationType) {
        world.selfServeNavigation.navigateToPage(applicationType, SelfServeSection.REVIEW_AND_DECLARATIONS);
        assertTrue(isTextPresent(interimOfferText));
        assertTrue(isElementPresent(interimRadioYes, SelectorType.XPATH));
    }

    @Then("i cannot request an interim on the {string}")
    public void iCannotRequestAnInterim(String applicationType) {
        world.selfServeNavigation.navigateToPage(applicationType, SelfServeSection.REVIEW_AND_DECLARATIONS);
        assertFalse(isTextPresent(interimOfferText));
        assertFalse(isElementPresent(interimRadioYes, SelectorType.XPATH));
    }

    @And("i request an interim authority on the {string}")
    public void iRequestAnInterimAuthority(String applicationType) {
        world.selfServeNavigation.navigateToPage(applicationType, SelfServeSection.REVIEW_AND_DECLARATIONS);
        click("(//input[@name='declarationsAndUndertakings[declarationConfirmation]'])[2]", SelectorType.XPATH);
        click(interimRadioYes, SelectorType.XPATH);
        waitAndEnterText("applicationInterimReason", SelectorType.ID, "I request an interim");
        waitAndClick("submit", SelectorType.ID);
    }
}