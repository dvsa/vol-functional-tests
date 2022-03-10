package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.openqa.selenium.TimeoutException;

import static junit.framework.TestCase.assertTrue;
import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InternalApplication extends BasePage implements En {
    private final World world;

    private final String vehicleAuthorisation = "//dt[contains(text(),'Total vehicle authorisation')]/../dd";
    private final String numberOfVehicles = "//dt[contains(text(),'No. of vehicles')]/../dd";
    private final String numberOfOperatingCentres = "//dt[contains(text(),'No. of operating centres')]/../dd";

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
        String docStoreLink = getText("letter-link",SelectorType.ID);
        assertNotNull(docStoreLink);
        String webDAVUrl = URL.build(ApplicationType.INTERNAL, world.configuration.env, "documents-dav").toString();
        assertTrue(docStoreLink.contains(String.format("ms-word:ofe|u|%s",webDAVUrl)));
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

    @And("I navigate to the application overview")
    public void iNavigateToTheApplicationOverview() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getApplication();
    }

    @Then("the LGV Only authorisation should be correct on the application overview screen")
    public void theLGVOnlyAuthorisationShouldBeCorrectOnTheApplicationOverviewScreen() {
        String LGVOnlyAuthorisation = "//dt[contains(text(),'Total Light goods vehicle authorisation')]/../dd";
        String actualLGVAuthorisation = getText(LGVOnlyAuthorisation, SelectorType.XPATH);
        String expectedLGVAuthority = String.format("0 (%s)", world.createApplication.getTotalOperatingCentreLgvAuthority());
        assertEquals(expectedLGVAuthority, actualLGVAuthorisation);

        String actualNoOfVehicles = getText(numberOfVehicles, SelectorType.XPATH);
        String expectedNoOfVehicles = String.format("0 (%s)", world.createApplication.getNoOfAddedLgvVehicles());
        assertEquals(actualNoOfVehicles, expectedNoOfVehicles);

        String actualNumberOfOperatingCentres = getText(numberOfOperatingCentres, SelectorType.XPATH);
        String expectedNumberOfOperatingCentres = "0 (0)";
        assertEquals(actualNumberOfOperatingCentres, expectedNumberOfOperatingCentres);
    }

    @Then("the PSV Standard International authorisation should be correct on the application overview screen")
    public void thePSVStandardInternationalAuthorisationShouldBeCorrectOnTheApplicationOverviewScreen() {
        String actualVehicleAuthorisation = getText(vehicleAuthorisation, SelectorType.XPATH);
        String expectedVehicleAuthority = String.format("0 (%s)", world.createApplication.getTotalOperatingCentreHgvAuthority());
        assertEquals(expectedVehicleAuthority, actualVehicleAuthorisation);

        String actualNoOfVehicles = getText(numberOfVehicles, SelectorType.XPATH);
        String expectedNoOfVehicles = String.format("0 (%s)", world.createApplication.getNoOfAddedHgvVehicles());
        assertEquals(actualNoOfVehicles, expectedNoOfVehicles);

        String actualNumberOfOperatingCentres = getText(numberOfOperatingCentres, SelectorType.XPATH);
        String expectedNumberOfOperatingCentres = "0 (1)";
        assertEquals(actualNumberOfOperatingCentres, expectedNumberOfOperatingCentres);
    }

    @Then("the Goods Standard National authorisation should be correct on the application overview screen")
    public void theGoodsStandardNationalAuthorisationShouldBeCorrectOnTheApplicationOverviewScreen() {
        String actualHGVAuthorisation = getText(vehicleAuthorisation, SelectorType.XPATH);
        String expectedHGVAuthority = String.format("0 (%s)", world.createApplication.getTotalOperatingCentreHgvAuthority());
        assertEquals(expectedHGVAuthority, actualHGVAuthorisation);

        String trailerAuthorisation = "//dt[contains(text(),'Total trailer authorisation')]/../dd";
        String actualTrailerAuthorisation = getText(trailerAuthorisation, SelectorType.XPATH);
        String expectedTrailerAuthority = String.format("0 (%s)", world.createApplication.getTotalOperatingCentreTrailerAuthority());
        assertEquals(actualTrailerAuthorisation, expectedTrailerAuthority);

        String actualNoOfVehicles = getText(numberOfVehicles, SelectorType.XPATH);
        String expectedNoOfVehicles = String.format("0 (%s)", world.createApplication.getNoOfAddedHgvVehicles());
        assertEquals(actualNoOfVehicles, expectedNoOfVehicles);

        String actualNumberOfOperatingCentres = getText(numberOfOperatingCentres, SelectorType.XPATH);
        String expectedNumberOfOperatingCentres = "0 (1)";
        assertEquals(actualNumberOfOperatingCentres, expectedNumberOfOperatingCentres);
    }

    @And("I click cancel")
    public void clicksCancel() {
        world.UIJourney.clickCancel();
    }

    @And("the caseworker is still on the operators page")
    public void theCaseworkerIsStillOnTheOperatorsPage() {
        assertTrue(isTitlePresent(world.registerUser.getOrganisationName(), 10));
    }
}
