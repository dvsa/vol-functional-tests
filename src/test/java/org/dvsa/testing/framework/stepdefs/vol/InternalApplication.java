package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import junit.framework.Assert;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.TimeoutException;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.Assert.*;

public class InternalApplication extends BasePage implements En {
    private final World world;

    private String HGVOnlyAuthorisation = "//dt[contains(text(),'Total Heavy goods vehicle authorisation')]/../dd";
    private String LGVOnlyAuthorisation = "//dt[contains(text(),'Total Light goods vehicle authorisation')]/../dd";
    private String vehicleAuthorisation = "//dt[contains(text(),'Total vehicle authorisation')]/../dd";
    private String trailerAuthorisation = "//dt[contains(text(),'Total trailer authorisation')]/../dd";
    private String numberOfOperatingCentres = "//dt[contains(text(),'No. of operating centres')]/../dd";

    public InternalApplication (World world) {this.world = world;}

    @When("the caseworker completes and submits the application")
    public void theCaseworkerCompletesAndSubmitsTheApplication() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getApplication();
        click("//*[@id='menu-application-decisions-submit']", SelectorType.XPATH);
        waitAndClick("//*[@id='form-actions[submit]']", SelectorType.XPATH);
        waitForTextToBePresent("has been submitted");
        world.UIJourney.caseWorkerCompleteConditionsAndUndertakings();
        world.UIJourney.caseWorkerCompleteReviewAndDeclarations();
        world.UIJourney.caseWorkerCompleteOverview();
    }

    @And("grants the application")
    public void grantsTheApplication() {
        int tableColumns;
        waitAndClick("//*[@id='menu-application_fee']", SelectorType.XPATH);
        world.feeAndPaymentJourney.selectFee();
        String fee = getAttribute("details[maxAmountForValidator]", SelectorType.ID, "value").toString();
        world.feeAndPaymentJourney.payFee(fee, "cash");
        waitForTextToBePresent("The payment was made successfully");
        long kickoutTime = System.currentTimeMillis() + 15000;

        do {
            tableColumns = returnTableRows("//tbody/tr/*",SelectorType.XPATH);
            refreshPageWithJavascript();
        } while (tableColumns > 1 && System.currentTimeMillis() < kickoutTime);

        if (System.currentTimeMillis() > kickoutTime) {
            throw new TimeoutException("Kickout time for expecting no fee is present when granting a licence exceeded.");
        }
        world.UIJourney.grantApplicationUnderDelegatedAuthority();
    }

    @Then("the licence is granted in Internal")
    public void theLicenceIsGrantedInInternal() {
        waitForTextToBePresent("Overview");
        world.UIJourney.checkLicenceStatus("Granted");
    }

    @When("i generate a letter")
    public void iGenerateALetter() {
        world.UIJourney.generateLetter();
    }



    @Then("The pop up should contain letter details")
    public void thePopUpShouldContainLetterDetails() {
        waitForTextToBePresent("Amend letter");

        String categoryValue = getText("//*[@id='generate-document']/div[2]", SelectorType.XPATH);
        assertNotNull(categoryValue);

        String subCategoryValue = getText("//*[@id='generate-document']/div[3]", SelectorType.XPATH);
        assertNotNull(subCategoryValue);

        String templateValue = getText("//*[@id='generate-document']/div[4]", SelectorType.XPATH);
        assertNotNull(templateValue);

        String docStoreLink = getAttribute("//a[contains(@href,'ms-word:ofe|u|https://')]", SelectorType.XPATH, "href");
        assertNotNull(docStoreLink);
        assertTrue(docStoreLink.contains(".rtf"));
    }

    @Then("the postcode warning message should be displayed on internal")
    public void thePostcodeWarningMessageShouldBeDisplayedOnInternal() {
        assertTrue(isTextPresent("This operating centre is in a different traffic area from the other centres."));
        click("form-actions[confirm-add]", SelectorType.ID);
        click("form-actions[submit]", SelectorType.ID);
        waitForTextToBePresent("Operating centres and authorisation");
        assertTrue(isElementPresent("//input[@value='2 MAR PLACE, ALLOA, FK10 1AA']", SelectorType.XPATH));
    }

    @When("a caseworker adds a new operating centre out of the traffic area")
    public void aCaseworkerAddsANewOperatingCentreOutOfTheTrafficArea() {
        world.UIJourney.addNewOperatingCentre();
    }

    @And("i save the letter")
    public void iSaveTheLetter() {
        click("//*[@id='form-actions[submit]']",SelectorType.XPATH);
        waitForTextToBePresent("Send letter");
        click("//*[@id='close']",SelectorType.XPATH);
        waitForTextToBePresent("The document has been saved");
    }


    @And("I delete generated letter above from the table")
    public void iDeleteGeneratedLetterAboveFromTheTable() {
        world.UIJourney.deleteLicenceDocument();
    }

    @Then("the document should be deleted")
    public void theDocumentShouldBeDeleted() {
        waitForTextToBePresent("Deleted successfully");
    }

    @Then("the LGV Only authorisation on the application overview screen should display {string} lgvs to {string} lgvs")
    public void theLGVOnlyAuthorisationOnTheApplicationOverviewScreenShouldDisplayLgvsToLgvs(String oldAuthorisation, String newAuthorisation) {
        checkExpectedValuesOnApplicationOverview(LGVOnlyAuthorisation, oldAuthorisation, newAuthorisation);

        assertFalse(isTextPresent("Total Heavy goods vehicle authorisation"));
        assertFalse(isTextPresent("Total vehicle authorisation"));
    }

    @Then("the LGV Mixed authorisation on the application overview screen should display {string} hgvs to {string} hgvs and {string} lgvs to {string} lgvs")
    public void theLGVMixedAuthorisationShouldBeCorrectOnTheApplicationOverviewScreenShouldDisplayHgvsToHgvsAndLgvsToLgvs(String oldHgvAuthorisation, String newHgvAuthorisation, String oldLgvAuthorisation, String newLgvAuthorisation) {
        checkExpectedValuesOnApplicationOverview(HGVOnlyAuthorisation, oldHgvAuthorisation, newHgvAuthorisation);

        checkExpectedValuesOnApplicationOverview(LGVOnlyAuthorisation, oldLgvAuthorisation, newLgvAuthorisation);

        assertFalse(isTextPresent("Total vehicle authorisation"));
    }

    @Then("the Goods Standard National authorisation on the application overview screen should display {string} vehicles to {string} vehicles and {string} trailers to {string} trailers")
    public void theGoodsStandardNationalAuthorisationOnTheApplicationOverviewScreenShouldDisplayVehiclesToVehicles(String oldAuthorisation, String newAuthorisation, String oldTrailers, String newTrailers) {
        checkExpectedValuesOnApplicationOverview(vehicleAuthorisation, oldAuthorisation, newAuthorisation);

        checkExpectedValuesOnApplicationOverview(trailerAuthorisation, oldTrailers, newTrailers);

        assertFalse(isTextPresent("Total Heavy goods vehicle authorisation"));
        assertFalse(isTextPresent("Total Light goods vehicle authorisation"));
    }

    @Then("the PSV Standard International authorisation on the application overview screen should display {string} vehicles to {string} vehicles")
    public void thePSVStandardInternationalAuthorisationOnTheApplicationOverviewScreenShouldDisplayVehiclesToVehicles(String oldAuthorisation, String newAuthorisation) {
        checkExpectedValuesOnApplicationOverview(vehicleAuthorisation, oldAuthorisation, newAuthorisation);

        assertFalse(isTextPresent("Total Heavy goods vehicle authorisation"));
        assertFalse(isTextPresent("Total Light goods vehicle authorisation"));
        assertFalse(isTextPresent("Total trailer authorisation"));
    }

    @And("the application overview displays {string} operating centres to {string} operating centres")
    public void theApplicationOverviewDisplaysOperatingCentresToOperatingCentres(String oldNumberOfOperatingCentres, String newNumberOfOperatingCentres) {
        checkExpectedValuesOnApplicationOverview(numberOfOperatingCentres, oldNumberOfOperatingCentres, newNumberOfOperatingCentres);
    }

    @And("I click cancel")
    public void clicksCancel() {
        world.UIJourney.clickCancel();
    }

    @And("the caseworker is still on the operators page")
    public void theCaseworkerIsStillOnTheOperatorsPage() {
        assertTrue(isTitlePresent(world.registerUser.getOrganisationName(), 10));
    }

    private void checkExpectedValuesOnApplicationOverview(String location, String oldValue, String newValue) {
        String actualText = getText(location, SelectorType.XPATH);
        String expectedText;
        if (oldValue.equals(newValue) && !location.equals(numberOfOperatingCentres))
            expectedText = oldValue;
        else
            expectedText = String.format("%s (%s)", oldValue, newValue);
        assertEquals(actualText, expectedText);
    }

}
