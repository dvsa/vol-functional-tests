package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.number.Int;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dvsa.testing.framework.Journeys.licence.OperatingCentreJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import scanner.AXEScanner;

import java.io.IOException;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class OperatingCentreVariation extends BasePage {
    World world;

    // Optimal to head towards page objects method for storing selectors for each page but not sure on best design.
    String confirmDeclaration =  "//input[@id='declarationsAndUndertakings[declarationConfirmation]']";
    String submitApplication = "//button[@id='submit']";
    String submitAndPayForApplication = "//button[@id='submitAndPay']";

    String payNow = "//button[@id='form-actions[pay]']";

    String numberOfNewOperatingCentreVehicles;


    public OperatingCentreVariation(World world) {
        this.world = world;
    }

    @And("i create an operating centre variation with {string} hgvs and {string} lgvs")
    public void iCreateAnOperatingCentreVariationWithHgvAndLgvs(String newHGVTotalAuthority, String newLGVTotalAuthority) {
        world.operatingCentreJourney.loginAndSaveOperatingCentreVehicleAuthorisationVariationChange(newHGVTotalAuthority, newLGVTotalAuthority, String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority()));
    }

    @And("i create and submit an operating centre variation with {string} hgvs and {string} lgvs")
    public void iCreateAndSubmitAnOperatingCentreVariationWithHgvsAndLgvs(String numberOfHGVs, String numberOfLGVs) {
        world.operatingCentreJourney.loginAndSubmitOperatingCentreVehicleAuthorisationVariationApplication(numberOfHGVs, numberOfLGVs);
    }

    @And("i create and submit and grant an operating centre variation with {string} hgvs and {string} lgvs")
    public void iCreateAndSubmitAndGrantAnOperatingCentreVariationWithHgvsAndLgvs(String numberOfHGVs, String numberOfLGVs) {
        world.operatingCentreJourney.loginAndSubmitOperatingCentreVehicleAuthorisationVariationApplication(numberOfHGVs, numberOfLGVs);
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getVariationApplication();
        world.UIJourney.caseWorkerCompleteOverview();
        waitForTextToBePresent("The overview page has been saved");
        world.UIJourney.grantApplicationUnderDelegatedAuthority();
    }

    @And("complete the financial evidence page")
    public void completeTheFinancialEvidenceAndReviewAndDeclarationShouldDisplayPayAndSubmit() {
        world.UIJourney.completeFinancialEvidencePage();
    }

    @And("the review and declaration page should display pay and submit")
    public void completeTheReviewAndDeclarationPageShouldDisplayPayAndSubmit() {
        clickByLinkText("Review and declarations");
        click(confirmDeclaration, SelectorType.XPATH);
        assertTrue(isElementPresent(submitAndPayForApplication, SelectorType.XPATH));
    }

    @And("the review and declaration page should only display submit application")
    public void completeTheFinancialEvidenceAndReviewAndDeclarationShouldDisplayOnlyDisplaySubmitApplication() {
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

    @When("i change my total HGV vehicle authority to {int} without changing the operating centres")
    public void iChangeMyTotalHGVVehicleAuthorityWithoutChangingTheOperatingCentres(int HGVTotalAuthority) {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
        world.UIJourney.changeLicenceForVariation();
        String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(String.valueOf(HGVTotalAuthority), "0", currentTrailerTotalAuthority);
    }

    @When("i add an operating centre and increase the vehicle total authority")
    public void iAddAnOperatingCentreAndIncreaseTheVehicleTotalAuthority() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
        world.UIJourney.changeLicenceForVariation();
        numberOfNewOperatingCentreVehicles = "5";
        world.operatingCentreJourney.addNewOperatingCentre( numberOfNewOperatingCentreVehicles, "0");
        String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        Integer newTotalVehicleAuthority = Integer.parseInt(numberOfNewOperatingCentreVehicles) + world.createApplication.getTotalOperatingCentreHgvAuthority();
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(String.valueOf(newTotalVehicleAuthority), "0", currentTrailerTotalAuthority);
    }


    @And("increase the authority on an existing operating centre authorisation and update the total authorisations")
    public void increaseTheAuthorityOnAnExistingOperatingCentreAuthorisationAndUpdateTheTotalAuthorisations() throws InterruptedException {
        world.selfServeNavigation.navigateToPage("variation", "Operating centres and authorisation");
        String updatedOperatingCentreVehicleAuthorisation = "10";
        world.operatingCentreJourney.updateOperatingCentreAuthorisation(updatedOperatingCentreVehicleAuthorisation);
        int newTotalHGVAuthorisation = Integer.parseInt(numberOfNewOperatingCentreVehicles) + Integer.parseInt(updatedOperatingCentreVehicleAuthorisation);
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(String.valueOf(newTotalHGVAuthorisation), "0", String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority()));
    }

    @When("i increase my lgv authorisation and delete the new operating centre")
    public void iIncreaseMyLgvAuthorisationAndDeleteTheNewOperatingCentre() throws InterruptedException {
        world.selfServeNavigation.navigateToPage("variation", "Operating centres and authorisation");
        click("//*[contains(@name, 'table[action][delete]')]", SelectorType.XPATH);
        waitForTextToBePresent("Are you sure you want to remove this operating centre?");
        click("//button[@id='form-actions[submit]']", SelectorType.XPATH);
        sleep(3000);
        String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority("5" , "5", currentTrailerTotalAuthority);
    }

    @When("i create a lgv authorisation increase variation with {string} on internal")
    public void iCreateALgvAuthorisationIncreaseVariationWithOnInternal(String feeRequired) {
        boolean variationFeeRequired = feeRequired.equals("Fee Required");
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalNavigation.getLicence();
        world.UIJourney.createVariationInInternal(variationFeeRequired);
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
        AXEScanner scanner = new AXEScanner();
        world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
        scanner.scan();
        world.UIJourney.changeLicenceForVariation();
        scanner.scan();
        String operatingCentreEditLink = String.format("//*[contains(@value,'%s')]", world.createApplication.getOperatingCentreAddressLine1());
        click(operatingCentreEditLink, SelectorType.XPATH);
        waitForTitleToBePresent("Edit operating centre");
        scanner.scan();
        click("//*[@class='govuk-back-link']", SelectorType.XPATH);
        waitForTitleToBePresent("Operating centres and authorisation");
        click(world.operatingCentreJourney.addOperatingCentre, SelectorType.XPATH);
        waitForTitleToBePresent("Add operating centre");
        scanner.scan();
    }
}
