package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.IllegalBrowserException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class OperatingCentreVariation extends BasePage {
    World world;

    // Optimal to head towards page objects method for storing selectors for each page but not sure on best design.
    String confirmDeclaration = "//input[@id='declarationsAndUndertakings[declarationConfirmation]']";
    String submitApplication = "//button[@id='submit']";
    String submitAndPayForApplication = "//button[@id='submitAndPay']";
    String payNow = "//button[@id='form-actions[pay]']";
    String numberOfNewOperatingCentreVehicles;


    public OperatingCentreVariation(World world) {
        this.world = world;
    }

    @And("i create an operating centre variation with {string} hgvs and {string} lgvs")
    public void iCreateAnOperatingCentreVariationWithHgvAndLgvs(String newHGVTotalAuthority, String newLGVTotalAuthority) {
        world.operatingCentreJourney.saveOperatingCentreVehicleAuthorisationVariationChange(newHGVTotalAuthority, newLGVTotalAuthority);
    }

    @And("i create and submit an operating centre variation with {string} hgvs and {string} lgvs")
    public void iCreateAndSubmitAnOperatingCentreVariationWithHgvsAndLgvs(String numberOfHGVs, String numberOfLGVs) {
          UniversalActions.clickHome();
        world.operatingCentreJourney.submitOperatingCentreVehicleAuthorisationVariationApplication(numberOfHGVs, numberOfLGVs);
    }

    @And("i create and submit and grant an operating centre variation with {string} hgvs and {string} lgvs")
    public void iCreateAndSubmitAndGrantAnOperatingCentreVariationWithHgvsAndLgvs(String numberOfHGVs, String numberOfLGVs) throws HttpException {
        world.operatingCentreJourney.submitOperatingCentreVehicleAuthorisationVariationApplication(numberOfHGVs, numberOfLGVs);
        world.internalNavigation.navigateToPage("variation", SelfServeSection.VIEW);
        world.internalUIJourney.caseWorkerCompleteOverview();
        waitForTextToBePresent("The overview page has been saved");
        world.internalUIJourney.grantApplicationUnderDelegatedAuthority();
    }

    @And("complete the financial evidence page")
    public void completeTheFinancialEvidenceAndReviewAndDeclarationShouldDisplayPayAndSubmit() {
        world.selfServeUIJourney.completeFinancialEvidencePage();
    }

    @And("the review and declaration page should display pay and submit")
    public void completeTheReviewAndDeclarationPageShouldDisplayPayAndSubmit() {
        clickByLinkText("Review and declarations");
        click(confirmDeclaration, SelectorType.XPATH);
        assertTrue(isElementPresent(submitAndPayForApplication, SelectorType.XPATH));
    }

    @And("the review and declaration page should only display submit application")
    public void theReviewAndDeclarationShouldDisplayOnlyDisplaySubmitApplication() {
        clickByLinkText("Review and declarations");
        click(confirmDeclaration, SelectorType.XPATH);
        assertTrue(isElementPresent(submitApplication, SelectorType.XPATH));
        assertFalse(isElementPresent(submitAndPayForApplication, SelectorType.XPATH));
    }

    @Then("the variation fee should be triggered")
    public void theVariationFeeShouldBeTriggered() {
        click(submitAndPayForApplication, SelectorType.XPATH);
        assertTrue(isTitlePresent("Variation Fee for application", 10));
        String invoiceAmount = getText("//li/dt[text()='Invoice amount']/../dd", SelectorType.XPATH);
        String outstandingAmount = getText("//li/dt[text()='Outstanding']/../dd", SelectorType.XPATH);
        assertEquals("£257.00", invoiceAmount);
        assertEquals("£257.00", outstandingAmount);
        assertTrue(isElementPresent(payNow, SelectorType.XPATH));
    }

    //@When("i change my total HGV vehicle authority to {int} without changing the operating centres")
    @When("i change my total vehicle authorities to {string} HGVs {string} LGVs and {string} trailers without changing the operating centres")
    //i change my total vehicle authoritiesy to {int} HGVs {int} LGVs and {int} trailers without changing the operating centres
    public void iChangeMyTotalHGVVehicleAuthorityWithoutChangingTheOperatingCentres(String HGVTotalAuthority, String LGVTotalAuthority, String TrailersTotalAuthority) {
        world.generalVariationJourney.beginOperatingCentreVariation();
        String newTrailerTotalAuthority;
        if (TrailersTotalAuthority.equals("same")) {
            newTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        } else {
            newTrailerTotalAuthority = TrailersTotalAuthority;
        }

        String newLGVTotalAuthority;
        if (LGVTotalAuthority.equals("same")) {
            newLGVTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority());
        } else {
            newLGVTotalAuthority = LGVTotalAuthority;
        }

        String newHGVTotalAuthority;
        if (HGVTotalAuthority.equals("same")) {
            newHGVTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreHgvAuthority());
        } else {
            newHGVTotalAuthority = HGVTotalAuthority;
        }

        //String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(newHGVTotalAuthority, newLGVTotalAuthority, newTrailerTotalAuthority);
    }

    @When("i add an operating centre and increase the vehicle total authority")
    public void iAddAnOperatingCentreAndIncreaseTheVehicleTotalAuthority() {
        world.generalVariationJourney.beginOperatingCentreVariation();
        numberOfNewOperatingCentreVehicles = "5";
        world.operatingCentreJourney.addNewOperatingCentre(numberOfNewOperatingCentreVehicles, "0");
        String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        Integer newTotalVehicleAuthority = Integer.parseInt(numberOfNewOperatingCentreVehicles) + world.createApplication.getTotalOperatingCentreHgvAuthority();
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(String.valueOf(newTotalVehicleAuthority), "0", currentTrailerTotalAuthority);
    }


    @And("increase the authority on an existing operating centre authorisation and update the total authorisations")
    public void increaseTheAuthorityOnAnExistingOperatingCentreAuthorisationAndUpdateTheTotalAuthorisations() {
        clickByLinkText("Operating centres and authorisation");
        String updatedOperatingCentreVehicleAuthorisation = "10";
        world.operatingCentreJourney.updateOperatingCentreAuthorisation(updatedOperatingCentreVehicleAuthorisation, String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority()));
        int newTotalHGVAuthorisation = Integer.parseInt(numberOfNewOperatingCentreVehicles) + Integer.parseInt(updatedOperatingCentreVehicleAuthorisation);
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(String.valueOf(newTotalHGVAuthorisation), "0", String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority()));
          UniversalActions.clickHome();
    }

    @When("i increase my lgv authorisation and delete the new operating centre")
    public void iIncreaseMyLgvAuthorisationAndDeleteTheNewOperatingCentre() throws InterruptedException {
        world.selfServeNavigation.navigateToPage("variation", SelfServeSection.OPERATING_CENTERS_AND_AUTHORISATION);
        click("//*[contains(@name, 'table[action][delete]')]", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure you want to remove this operating centre?");
        UniversalActions.clickSubmit();
        sleep(3000);
        String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority("5", "5", currentTrailerTotalAuthority);
    }

    @When("i create a lgv authorisation increase variation with {string} on internal")
    public void iCreateALgvAuthorisationIncreaseVariationWithOnInternal(String feeRequired) throws HttpException {
        boolean variationFeeRequired = feeRequired.equals("Fee Required");
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        world.internalUIJourney.createVariationInInternal(variationFeeRequired);
    }

    @Then("the variation fee is required on internal")
    public void theVariationFeeIsRequiredOnInternal() {
        clickByLinkText("Fees");
        assertEquals("£257.00", getText("//td[@data-heading='Fee amount']", SelectorType.XPATH));
        assertEquals("£257.00", getText("//td[@data-heading='Outstanding']", SelectorType.XPATH));
    }

    @Then("the variation fee is not required on internal")
    public void theVariationFeeIsNotRequiredOnInternal() {
        clickByLinkText("Fees");
        assertFalse(isTextPresent("//td[@data-heading='Fee amount']"));
        assertFalse(isTextPresent("//td[@data-heading='Outstanding']"));
        assertTrue(isTextPresent("There are no results matching your search"));
    }

    @And("i scan the various operating centre and authorisation pages")
    public void iScanTheVariousOperatingCentreAndAuthorisationPages() throws IllegalBrowserException, IOException {
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.OPERATING_CENTERS_AND_AUTHORISATION);
        world.selfServeUIJourney.changeLicenceForVariation();
        String operatingCentreEditLink = String.format("//*[contains(@value,'%s')]", world.createApplication.getOperatingCentreAddressLine1());
        click(operatingCentreEditLink, SelectorType.XPATH);
        waitForTitleToBePresent("Edit operating centre");
        click("//*[@class='govuk-back-link']", SelectorType.XPATH);
        waitForTitleToBePresent("Operating centres and authorisation");
        click(world.operatingCentreJourney.addOperatingCentre, SelectorType.XPATH);
        waitForTitleToBePresent("Add operating centre");
    }

    @And("i create an lgv authorisation variation with {int} more LGVs")
    public void iCreateAnLGVAuthorisationVariationWithMoreLGVs(int additionalAuthority) {
        world.generalVariationJourney.signInAndBeginLGVAuthorisationVariation();
        String newAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority() + additionalAuthority);
        world.operatingCentreJourney.updateLGVOnlyAuthorityAndSave(newAuthority);
    }

    @And("i create and submit an lgv authorisation variation with {int} more LGVs")
    public void iCreateAndSubmitAnLgvAuthorisationVariation(int additionalAuthority) {
        world.generalVariationJourney.signInAndBeginLGVAuthorisationVariation();
        String newAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority() + additionalAuthority);
        world.operatingCentreJourney.updateLGVOnlyAuthorityAndSave(newAuthority);
        world.operatingCentreJourney.completeLGVOnlyApplicationAfterUpdatingLGVAuthority(newAuthority);
    }

    @And("I create and save an lgv authorisation variation on internal with {int} more LGVs")
    public void iCreateAndSaveAnLgvAuthorisationVariationOnInternal(int additionalAuthority) throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
        world.internalUIJourney.createVariationInInternal(false);
        String newAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority() + additionalAuthority);
        world.internalNavigation.navigateToAuthorisationPage();
        world.operatingCentreJourney.updateLGVOnlyAuthorityAndSave(newAuthority);
    }

    @When("i create an operating centre variation with {int} trailers")
    public void iCreateAnOperatingCentreVariationWithTrailers(int newNumberOfTrailers) {
        world.operatingCentreJourney.saveOCVehicleAuthVarChange(
                String.valueOf(world.createApplication.getTotalOperatingCentreHgvAuthority()),
                String.valueOf(world.createApplication.getTotalOperatingCentreLgvAuthority()),
                String.valueOf(newNumberOfTrailers));
    }

    @When("i begin an operating centre and authorisation variation")
    public void iBeginAnOperatingCentreAndAuthorisationVariation() {
        world.generalVariationJourney.beginOperatingCentreVariation();
    }

    @And("i create a new operating centre with {string} vehicles and {string} trailers")
    public void iCreateANewOperatingCentreWithVehiclesAndTrailers(String numberOfVehicles, String numberOfTrailers) {
        world.operatingCentreJourney.addNewOperatingCentre(numberOfVehicles, numberOfTrailers);
    }

    @And("The variation is submitted")
    public void variationSubmitted() {
        world.selfServeUIJourney.completeFinancialEvidencePage();
        clickByLinkText("Review and declarations");
        click(confirmDeclaration, SelectorType.XPATH);
        click(submitAndPayForApplication, SelectorType.XPATH);
        UniversalActions.clickPay();
        world.feeAndPaymentJourney.customerPaymentModule();
        waitForTextToBePresent("Thank you, your application has been submitted.");
    }

    @Then("the operating centre cannot be added")
    public void theOperatingCentreCannotBeAdded() {
        assertTrue(isTextPresent("Add operating centre"));
        // add assertion on error message when/if validation error is fixed
    }

    @And("i increase total PSV authorisation to {string} vehicles")
    public void iIncreasePSVAuthorisation(String numberOfPSVVehicles) {
        replaceText("totAuthHgvVehicles", SelectorType.ID, numberOfPSVVehicles);
        UniversalActions.clickSaveAndReturn();
    }

    @Then("the increase in PSV authorisation is not allowed")
    public void theIncreaseInPSVAuthorisationIsNotAllowed() {
        assertTrue(isTextPresent("The total number of vehicles on a restricted licence cannot exceed 2"));
    }

    @And("the {string} {string} variation is submitted")
    public void theVariationIsSubmitted(String operatorType, String licenceType) {
        world.selfServeUIJourney.completeFinancialEvidencePage();
        world.psvJourney.completeVehicleSize();
        clickByLinkText("Review and declarations");
        click(confirmDeclaration, SelectorType.XPATH);
        click(submitAndPayForApplication, SelectorType.XPATH);
        UniversalActions.clickPay();
        world.feeAndPaymentJourney.customerPaymentModule();
    }
}
