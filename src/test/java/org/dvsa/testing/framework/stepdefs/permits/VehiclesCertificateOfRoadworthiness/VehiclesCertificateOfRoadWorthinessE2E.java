package org.dvsa.testing.framework.stepdefs.permits.VehiclesCertificateOfRoadworthiness;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.BasePermitPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.SubmittedPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness.*;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.junit.jupiter.api.Assertions.*;

public class VehiclesCertificateOfRoadWorthinessE2E {
    private final World world;

    public VehiclesCertificateOfRoadWorthinessE2E(World world) {
        this.world = world;
    }

    @And("I select Certificate of Roadworthiness for vehicles on the select permit page")
    public void iSelectCertificateOfRoadWorthinessForVehiclesOnTheSelectPermitPage() {
        world.basePermitJourney.clickToPermitTypePage();
        world.basePermitJourney.permitType(PermitType.CERTIFICATE_OF_ROADWORTHINESS_FOR_VEHICLES);
    }

    @Then("I select any licence number for Certificate of Roadworthiness for vehicles")
    public void iSelectAnyLicenceNumberForCertificateOfRoadWorthiness() {
        world.basePermitJourney.licencePage();
    }

    @Then("I check content and complete Registration number section and click save and continue")
    public void iCheckContentAndCompleteRegistrationNumber() {
        OverviewPageJourney.clickOverviewSection(OverviewSection.RegistrationNumber);
        VehicleRegistrationNumberPage.untilOnPage();
        assertEquals("Enter the vehicle registration number", VehicleRegistrationNumberPage.getPageHeading());
        BasePermitPageJourney.hasReferenceOnPage();
        VehicleRegistrationNumberPage.enterRegistrationNumber();
        BasePermitPage.saveAndContinue();
    }

    @Then("I check content and complete Certificate  of Compliance section and click save and continue")
    public void iCheckContentAndCompleteCertificate() {
        CertificateOfComplianceNumberPage.untilOnPage();
        String heading = CertificateOfComplianceNumberPage.getPageHeading();
        assertEquals("Enter the vehicle Certificate of Compliance number (optional)", heading);
        BasePermitPageJourney.hasReferenceOnPage();
        CertificateOfComplianceNumberPage.enterComplianceNumber("BD51SMR");
        BasePermitPage.saveAndContinue();
    }

    @Then("I check content and complete vehicle make and model section and click save and continue")
    public void iCheckContentAndCompleteVehicleMake() {
        MakeAndModelPage.untilOnPage();
        String heading = MakeAndModelPage.getPageHeading();
        assertEquals(heading, "Enter the vehicle make and model");
        BasePermitPageJourney.hasReferenceOnPage();
        MakeAndModelPage.enterMakeAndModel("BD51SMR");
        BasePermitPage.saveAndContinue();
    }

    @Then("I check content and complete Vehicle identification number section and click save and continue")
    public void iCheckContentAndCompleteVehicleIdentificationNumber() {
        VehicleIdentificationNumberPage.untilOnPage();
        assertEquals("Enter the vehicle identification number (VIN)", VehicleIdentificationNumberPage.getPageHeading());
        BasePermitPageJourney.hasReferenceOnPage();
        VehicleIdentificationNumberPage.enterIdentificationNumber();
        BasePermitPage.saveAndContinue();
    }

    @Then("I check content and complete Vehicle Engine number section and click save and continue")
    public void iCheckContentAndCompleteVehicleEngineNumberSection() {
        VehicleEngineNumberPage.untilOnPage();
        assertEquals("Enter the vehicle engine number", VehicleEngineNumberPage.getPageHeading());
        BasePermitPageJourney.hasReferenceOnPage();
        VehicleEngineNumberPage.enterEngineNumber();
        BasePermitPage.saveAndContinue();
    }

    @Then("I check content and complete vehicle engine type section and click save and continue")
    public void iCheckContentAndCompleteVehicleEngineTypeSectionAndClickSave() {
        VehicleEngineTypePage.untilOnPage();
        assertEquals("Enter the vehicle engine type", VehicleEngineTypePage.getPageHeading());
        BasePermitPageJourney.hasReferenceOnPage();
        VehicleEngineTypePage.enterEngineType();
        BasePermitPage.saveAndContinue();
    }

    @Then("I check content and complete MOT DATE section and click save and continue")
    public void iCheckContentAndCompleteMOTDATE() {
        VehicleMotPage.untilOnPage();
        assertEquals("Enter the vehicle's MOT expiry date", VehicleMotPage.getPageHeading());
        BasePermitPageJourney.hasReferenceOnPage();
        VehicleMotPage.enterMOTDate();
        BasePermitPage.saveAndContinue();
    }

    @Then("I check content and click save and continue on the Check Your Answers page")
    public void iCheckContentAndClickSaveAndContinueOnTheCheck() {
        CheckYourAnswerPage.untilOnPage();
        String heading = CheckYourAnswerPage.getPageHeading();
        assertEquals("Check your answers", heading);
        CheckYourAnswerPage.saveAndContinue();
    }

    @Then("I check content and Accept and continue on the Declaration page")
    public void iCheckContentAndAcceptAndContinue() {
        DeclarationPage.untilOnPage();
        DeclarationPageJourney.hasPageHeading();
        DeclarationPageJourney.hasCheckboxText();
        DeclarationPageJourney.completeDeclaration();
    }

    @Then("I check content of the Submitted page")
    public void iCheckContentOfTheSubmittedPage() {
        SubmittedPage.untilOnPage();
        SubmittedPageJourney.hasPageHeading();
        SubmittedPageJourney.hasSubHeading();
        assertTrue(SubmittedPage.isCertificateAdvisoryTextPresent());
        assertTrue(SubmittedPage.isWarningMessagePresent());
    }

    @Then("I am navigated back to the permits dashboard page with my application status shown as Valid")
    public void iAMNavigatedBackToThePermitsDashboard() {
        untilAnyPermitStatusMatch(PermitStatus.VALID);
    }

} //TODO Possible refactoring to be done here. See if it can be combined with trailers?


