package org.dvsa.testing.framework.stepdefs.permits.TrailersCertificateOfRoadworthiness;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.BasePermitPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.SubmittedPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness.*;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.jupiter.api.Assertions.*;

public class TrailersCertificateOfRoadWorthinessE2E {
    private final World world;

    public TrailersCertificateOfRoadWorthinessE2E(World world) {
        this.world = world;
    }

    @And("I select Certificate of Roadworthiness for trailers on the select permit page")
    public void iSelectCertificateOfRoadWorthiness() {
        clickToPermitTypePage(world);
        BasePermitJourney.permitType(PermitType.CERTIFICATE_OF_ROADWORTHINESS_FOR_TRAILERS);
    }

    @Then("I select any licence number for Certificate of Roadworthiness for trailers")
    public void iSelectAnyLicenceNumberForCertificateOfRoadWorthiness() {
        BasePermitJourney.licencePage(world);

    }

    @Then("I check content and complete Registration number section for  Certificate of Roadworthiness for trailers and click save and continue")
    public void iCheckContentAndCompleteRegistration() {
        OverviewPageJourney.clickOverviewSection(OverviewSection.RegistrationNumber);
        VehicleRegistrationNumberPage.untilOnPage();
        assertEquals("Enter the DVLA trailer registration number", VehicleRegistrationNumberPage.getPageHeading());
        // Check the new validation
        BasePermitPage.saveAndContinue();
        assertEquals("Enter the registration number plate", VehicleRegistrationNumberPage.getRequiredFieldValidation());
        BasePermitPageJourney.hasReferenceOnPage();
        VehicleRegistrationNumberPage.enterTrailerRegistrationNumber();

        BasePermitPage.saveAndContinue();
    }

    @Then("I check content and complete Certificate  of Compliance section Certificate of Roadworthiness for trailers and click save and continue")
    public void iCheckContentAndCompleteCertificateOfCompliance() {
        CertificateOfComplianceNumberPage.untilOnPage();
        String heading = CertificateOfComplianceNumberPage.getPageHeading();
        assertEquals("Enter the trailer Certificate of Compliance number (optional)", heading);
        BasePermitPageJourney.hasReferenceOnPage();
        CertificateOfComplianceNumberPage.enterComplianceNumber("BD51SMR");
        CertificateOfComplianceNumberPage.saveAndContinue();
    }

    @Then("I check content and complete vehicle make and model section Certificate of Roadworthiness for trailers and click save and continue")
    public void iCheckContentAndCompleteVehicleMakeAndModel() {
        MakeAndModelPage.untilOnPage();
        String heading = MakeAndModelPage.getPageHeading();
        assertEquals("Enter the trailer make and model", heading);
        BasePermitPageJourney.hasReferenceOnPage();
        MakeAndModelPage.enterMakeAndModel("BD51SMR");
        MakeAndModelPage.saveAndContinue();
    }

    @Then("I check content and complete Vehicle identification number section Certificate of Roadworthiness for trailers and click save and continue")
    public void iCheckContentAndCompleteVehicleIdentificationNumber() {
        VehicleIdentificationNumberPage.untilOnPage();
        assertEquals("Enter the trailer's vehicle identification number (VIN)", VehicleIdentificationNumberPage.getPageHeading());
        BasePermitPageJourney.hasReferenceOnPage();
        VehicleIdentificationNumberPage.enterIdentificationNumber();
        BasePermitPage.saveAndContinue();
    }

    @Then("I check content and complete MOT DATE section Certificate of Roadworthiness for trailers and click save and continue")
    public void iCheckContentAndCompleteMOTDATE() {
        VehicleMotPage.untilOnPage();
        assertEquals("Enter the trailer's MOT expiry date", VehicleMotPage.getPageHeading());
        BasePermitPageJourney.hasReferenceOnPage();
        VehicleMotPage.enterMOTDate();
        BasePermitPage.saveAndContinue();
    }

    @Then("I check content and Accept and continue on the Declaration page for Certificate of Roadworthiness for trailers page")
    public void iCheckContentAndAcceptAndContinue() {
        DeclarationPage.untilOnPage();
        DeclarationPageJourney.hasPageHeading();
        assertTrue(DeclarationPage.isTrailersCertificateAdvisoryMessagePresent());
        DeclarationPageJourney.hasCheckboxText();
        DeclarationPageJourney.completeDeclaration();
    }

    @Then("I check content of the Submitted page for Certificate of Roadworthiness for trailers")
    public void iCheckContentOfTheSubmittedPageForCertificate() {
        SubmittedPage.untilOnPage();
        SubmittedPageJourney.hasPageHeading();
        SubmittedPageJourney.hasSubHeading();
        assertTrue(SubmittedPage.isCertificateAdvisoryTextPresent());
        assertTrue(SubmittedPage.isWarningMessagePresent());
    }

    @Then("I am navigated back to the permits dashboard page for Certificate of Roadworthiness for trailers with my application status shown as Valid")
    public void iAMNavigatedBackToThePermitsDashboard() {
        untilAnyPermitStatusMatch(PermitStatus.VALID);
    }
}