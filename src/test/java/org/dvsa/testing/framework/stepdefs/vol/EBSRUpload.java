package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.refreshPageWithJavascript;
import static org.dvsa.testing.framework.stepdefs.vol.ManageApplications.existingLicenceNumber;
import static org.junit.jupiter.api.Assertions.*;

public class EBSRUpload extends BasePage {
    private final World world;

    public EBSRUpload(World world) {
        this.world = world;
    }

    @Then("A short notice flag should be displayed in selfserve")
    public void aShortNoticeFlagShouldBeDisplayedInSelfserve() throws IllegalBrowserException, IOException, InterruptedException {
        world.busRegistrationJourney.viewEBSRInExternal();
        world.selfServeNavigation.refreshUntilSuccessfulOrTimeout();
        waitForTextToBePresent("Successful");
        assertTrue(isElementPresent("//strong[@class='govuk-tag govuk-tag--green' and contains(text(),'Successful')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//strong[@class='govuk-tag govuk-tag--orange' and contains(text(),'New')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//strong[@class='govuk-tag govuk-tag--orange' and contains(text(),'Short notice')]", SelectorType.XPATH));
    }

    @Then("A short notice flag should not be displayed in selfserve")
    public void aShortNoticeFlagShouldNotBeDisplayedInSelfserve() throws IllegalBrowserException, IOException, InterruptedException {
        world.busRegistrationJourney.viewEBSRInExternal();
        world.selfServeNavigation.refreshUntilSuccessfulOrTimeout();
        waitForTextToBePresent("Successful");
        assertTrue(isElementPresent("//strong[@class='govuk-tag govuk-tag--green' and contains(text(),'Successful')]", SelectorType.XPATH));
        assertTrue(isElementPresent("//strong[@class='govuk-tag govuk-tag--orange' and contains(text(),'New')]", SelectorType.XPATH));
        assertFalse(isElementPresent("//strong[@class='govuk-tag govuk-tag--orange' and contains(text(),'Short notice')]", SelectorType.XPATH));
    }

    @And("i add a new bus registration")
    public void iAddANewBusRegistration() {
        world.internalUIJourney.manualBusRegistration(0, 5, 0);
    }

    @And("i add a manual bus registration with service details")
    public void iAddAManualBusRegistrationWithServiceDetails(DataTable serviceDetails) {
        world.busRegistrationJourney.addManualBusRegistrationWithServiceDetails(serviceDetails.asMap(String.class, String.class));
    }

    @When("I save the short notice reason and comment")
    public void iSaveTheShortNoticeReasonAndComment(DataTable reasonDetails) {
        world.busRegistrationJourney.saveManualShortNoticeReason(reasonDetails.asMap(String.class, String.class));
    }

    @Then("the short notice reason and comment should remain saved")
    public void theShortNoticeReasonAndCommentShouldRemainSaved(DataTable expectedReasonDetails) {
        world.busRegistrationJourney.verifyManualShortNoticeReasonIsSaved(
                expectedReasonDetails.asMap(String.class, String.class)
        );
    }

    @When("I complete the short notice registration checks")
    public void iCompleteTheShortNoticeRegistrationChecks(DataTable expectedChecks) {
        world.busRegistrationJourney.completeManualShortNoticeRegistrationChecks(
                expectedChecks.asMap(String.class, String.class)
        );
    }

    @Then("the short notice registration checks should remain saved")
    public void theShortNoticeRegistrationChecksShouldRemainSaved(DataTable expectedChecks) {
        world.busRegistrationJourney.verifyManualShortNoticeRegistrationChecksAreSaved(
                expectedChecks.asMap(String.class, String.class)
        );
    }

    @When("I create a variation of the manual bus registration")
    public void iCreateAVariationOfTheManualBusRegistration() {
        world.busRegistrationJourney.createManualBusRegistrationVariation();
    }

    @Then("the new bus registration variation should inherit the original service details")
    public void theNewBusRegistrationVariationShouldInheritTheOriginalServiceDetails() {
        world.busRegistrationJourney.verifyManualBusRegistrationVariationInheritedServiceDetails();
    }

    @When("I update the bus registration variation with service details")
    public void iUpdateTheBusRegistrationVariationWithServiceDetails(DataTable serviceDetails) {
        world.busRegistrationJourney.updateManualBusRegistrationVariation(serviceDetails.asMap(String.class, String.class));
    }

    @When("I update the short-notice bus registration variation with service details")
    public void iUpdateTheShortNoticeBusRegistrationVariationWithServiceDetails(DataTable serviceDetails) {
        world.busRegistrationJourney.updateManualShortNoticeBusRegistrationVariation(
                serviceDetails.asMap(String.class, String.class)
        );
    }

    @Then("the variation should retain the updated service details and the original registration should remain unchanged")
    public void theVariationShouldRetainTheUpdatedServiceDetailsAndTheOriginalRegistrationShouldRemainUnchanged(
            DataTable serviceDetails
    ) {
        world.busRegistrationJourney.verifyManualBusRegistrationVariationServiceDetails(
                serviceDetails.asMap(String.class, String.class)
        );
    }

    @When("I grant the manual bus registration variation")
    public void iGrantTheManualBusRegistrationVariation(DataTable grantDetails) {
        world.busRegistrationJourney.grantManualBusRegistrationVariation(
                grantDetails.asMap(String.class, String.class)
        );
    }

    @Then("the manual registration history should show")
    public void theManualRegistrationHistoryShouldShow(DataTable expectedRows) {
        world.busRegistrationJourney.verifyManualBusRegistrationHistory(expectedRows.asMaps(String.class, String.class));
    }

    @When("I create a cancellation for the manual bus registration")
    public void iCreateACancellationForTheManualBusRegistration() {
        world.busRegistrationJourney.createManualBusCancellation();
    }

    @Then("the cancellation should inherit the saved service details")
    public void theCancellationShouldInheritTheSavedServiceDetails() {
        world.busRegistrationJourney.verifyManualBusCancellationInheritedServiceDetails();
    }

    @When("I save the cancellation dates")
    public void iSaveTheCancellationDates(DataTable expectedDates) {
        world.busRegistrationJourney.saveManualBusCancellationDates(
                expectedDates.asMap(String.class, String.class)
        );
    }

    @Then("the cancellation dates should remain saved")
    public void theCancellationDatesShouldRemainSaved() {
        world.busRegistrationJourney.verifyManualBusCancellationDatesAreSaved();
    }

    @When("I complete the manual bus cancellation registration checks")
    public void iCompleteTheManualBusCancellationRegistrationChecks(DataTable expectedChecks) {
        world.busRegistrationJourney.completeManualBusCancellationRegistrationChecks(
                expectedChecks.asMap(String.class, String.class)
        );
    }

    @Then("the manual bus cancellation registration checks should remain saved")
    public void theManualBusCancellationRegistrationChecksShouldRemainSaved(DataTable expectedChecks) {
        world.busRegistrationJourney.verifyManualBusCancellationRegistrationChecksAreSaved(
                expectedChecks.asMap(String.class, String.class)
        );
    }

    @When("I grant the manual bus cancellation")
    public void iGrantTheManualBusCancellation() {
        world.busRegistrationJourney.grantManualBusCancellation();
    }

    @Then("the manual bus cancellation should remain cancelled after reload")
    public void theManualBusCancellationShouldRemainCancelledAfterReload() {
        world.busRegistrationJourney.verifyManualBusCancellationRemainsCancelled();
    }

    @When("it has been paid and granted")
    public void itHasBeenPaidAndGranted() {
        world.busRegistrationJourney.payFeeAndGrantManualBusRegistration();
    }

    @Then("the bus registration should be granted")
    public void theBusRegistrationShouldBeGranted() {
        world.busRegistrationJourney.verifyManualBusRegistrationIsGranted();
    }

    @And("the traffic areas should be displayed on the service details page")
    public void theTrafficAreasShouldBeDisplayedOnTheServiceDetailsPage() {
        waitAndClickByLinkText("Service details");
        waitAndClickByLinkText("TA's");
        String trafficArea = findElement("//*[@id='bus-reg-ta']", SelectorType.XPATH, 10).getText();
        assertNotNull(trafficArea);
    }

    @And("Documents are generated")
    public void documentsAreGenerated() throws IllegalBrowserException, IOException {
        String licenceNumber = world.applicationDetails.getLicenceNumber();

        waitAndClick(String.format("//*[contains(text(),'%s')]", licenceNumber), SelectorType.XPATH);

        if (isElementPresent("//*[contains(text(),'View bus')]", SelectorType.XPATH)) {
            waitAndClick("//*[contains(text(),'View bus')]", SelectorType.XPATH);
        }

        if (!world.configuration.env.toString().equals("local")) {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(240)); // 4 minutes

            try {
                boolean allDocsGenerated = wait.until(driver -> {
                    refreshPageWithJavascript();

                    List<WebElement> generatedFiles = findElements("//*[@class='field file-upload']", SelectorType.XPATH);
                    if (generatedFiles.size() < 2) {
                        return false;
                    }

                    boolean hasDVSA = generatedFiles.stream().anyMatch(el -> el.getText().contains("DVSA Record PDF"));
                    boolean hasMap = generatedFiles.stream().anyMatch(el -> el.getText().contains("Route Track Map PDF (Auto Scale)"));

                    return hasDVSA && hasMap;
                });

                assertTrue(allDocsGenerated, "All required documents were not generated.");
            } catch (TimeoutException e) {
                throw new NotFoundException("One or more documents were not generated in the expected time.");
            }
        }
    }

    @Then("all Service Details fields should be editable")
    public void allServiceDetailsFieldsShouldBeEditable() {
        waitAndClickByLinkText("Service details");
        world.busRegistrationJourney.internalSiteEditBusReg();
    }

    @And("the edited Bus Registration details should be saved")
    public void theEditedBusRegistrationDetailsShouldBeSaved() {
        world.selfServeNavigation.navigateToBusRegExternal();
        waitForUrlToContain("search/find-registered-local-bus-services/details/", 3);
        assertTrue(isTextPresent("1234"));
    }
}
