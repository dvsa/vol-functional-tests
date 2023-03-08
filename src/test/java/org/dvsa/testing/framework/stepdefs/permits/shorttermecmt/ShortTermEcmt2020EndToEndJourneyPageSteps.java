package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;


import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.*;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortTermEcmt2020EndToEndJourneyPageSteps extends BasePage {
    private final World world;

    public ShortTermEcmt2020EndToEndJourneyPageSteps(World world) {
        this.world = world;
    }

    @Then("I select year on the select year page")
    public void iSelectYearOnTheSelectPage() {
        world.yearSelectionPage.selectShortTermValidityPeriod();
    }

    @Then("I select any licence number for short term permit")
    public void iSelectAnyLicenceNumber() {
        world.basePermitJourney.licencePage();
    }

    @Then("I complete the Check if you need ECMT permits section and click save and continue")
    public void iCompletedTheCheckIfYouNeedECMT() {
        OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
        CheckIfYouNeedECMTPermitsPageJourney.completePage();
    }

    @Then("I complete Certificates required page section and click save and continue")
    public void iCompleteCertificatesRequiredPage() {
        String heading = CertificatesRequiredPage.getPageHeading();
        assertEquals("Mandatory certificates for vehicles and trailers you intend to use", heading);
        CertificatesRequiredPageJourney.completePage();
    }

    @Then("I complete Countries with limited permits section and click save and continue")
    public void iCompleteCountriesWithLimitedPermits() {
        world.countriesWithLimitedPermitsPage.chooseNoCountriesWithLimitedPermits();
        BasePermitPage.saveAndContinue();
    }

    @Then("I complete Number of permits required section and click save and continue")
    public void iCompleteNumberOfPermitsRequired() {
        NumberOfPermitsPageJourney.hasPageHeading();
        NumberOfPermitsPageJourney.completeECMTPage();
    }

    @Then("^I complete Euro emissions standard page section and click save and continue")
    public void iCompleteEuroEmissionsStandardPage() {
        EmissionStandardsPageJourney.hasPageHeading();
        EmissionStandardsPageJourney.completePage();
    }

    @Then("I click confirm and continue on the Check your answers page")
    public void iClickConfirmAndContinueOnTheCheckPage(){
     CheckYourAnswerPage.saveAndContinue();
    }

    @Then("I click on Accept and continue on the Declaration page")
    public void iClickOnAcceptAndContinueOn() {
        DeclarationPageJourney.completeDeclaration();
    }

    @Then("I click on Submit and Pay button on the Permit fee page and complete the payment")
    public void iClickOnSubmitAndPayButtonOnThePermit() {
        world.permitFeePage.submitAndPay();
        world.feeAndPaymentJourney.customerPaymentModule();
        SubmittedPage.untilOnPage();
    }

    @Then("I click on the Finish button on the Application submitted page")
    public void iClickOnTheFinishButton() {
        SubmittedPage.untilOnPage();
        SubmittedPageJourney.hasPageHeading();
        waitForElementToBePresent("//a[contains(text(),'Go to permits dashboard')]");
        SubmittedPage.goToPermitsDashboard();
    }

    @Then("I am navigated back to the permits dashboard page with my application status shown as Under Consideration")
    public void iAmNavigatedBackToThePermitsDashboard() {
        HomePage.PermitsTab.selectFirstOngoingApplication();
        assertEquals(getElementValueByText("//span[@class='status orange']", SelectorType.XPATH), "UNDER CONSIDERATION");
    }
}