package org.dvsa.testing.framework.stepdefs.permits.VehiclesCertificateOfRoadworthiness;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.VehiclesCertificateOfRoadworthinessJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CheckYourAnswerPage;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.permits.pages.SubmittedPage;
import org.dvsa.testing.lib.newPages.permits.pages.vehiclesAndTrailersCertificateOfRoadworthiness.CertificateOfComplianceNumberPage;
import org.dvsa.testing.lib.newPages.permits.pages.vehiclesAndTrailersCertificateOfRoadworthiness.MakeAndModelPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.vehiclesCertificateOfRoadworthiness.*;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.hasCheckBoxText;
import static org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage.confirmDeclaration;


public class VehiclesCertificateOfRoadWorthinessE2E implements En {
    public VehiclesCertificateOfRoadWorthinessE2E(OperatorStore operatorStore, World world) {
        LicenceStore licenceStore = operatorStore.getCurrentLicence().orElseGet(LicenceStore::new);
        And("^I select Certificate of Roadworthiness for vehicles on the select permit page$", () -> {
            clickToPermitTypePage(world);
            VehiclesCertificateOfRoadworthinessJourney.getInstance().permitType(PermitType.CERTIFICATE_OF_ROADWORTHINESS_FOR_VEHICLES, operatorStore);
        });

        Then("^I select any licence number for Certificate of Roadworthiness for vehicles$", () -> {
            VehiclesCertificateOfRoadworthinessJourney.getInstance().licencePage(operatorStore, world);
        });

        Then("^I am on Certificate of Roadworthiness for vehicles Application overview Page$", org.dvsa.testing.lib.newPages.permits.pages.OverviewPage::untilOnPage);

        Then("^I check content and complete Registration number section and click save and continue$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.RegistrationNumber);
            VehicleRegistrationNumberPage.untilOnRegistrationPage();
            VehicleRegistrationNumberPage.hasPageHeading();
            BasePermitPage.getReferenceFromPage();
            VehicleRegistrationNumberPage.registrationNumber();
            BasePermitPage.saveAndContinue();
        });

        Then("^I check content and complete Certificate  of Compliance section and click save and continue$", () -> {
            CertificateOfComplianceNumberPage.untilOnPage();
            String heading = CertificateOfComplianceNumberPage.getPageHeading();
            Assert.assertEquals("Enter the vehicle Certificate of Compliance number (optional)", heading);
            BasePermitPage.getReferenceFromPage();
            CertificateOfComplianceNumberPage.enterComplianceNumber("BD51SMR");
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and complete vehicle make and model section and click save and continue$", () -> {
            MakeAndModelPage.untilOnPage();
            String heading = MakeAndModelPage.getPageHeading();
            Assert.assertEquals(heading,"Enter the vehicle make and model");
            BasePermitPage.getReferenceFromPage();
            MakeAndModelPage.enterMakeAndModel("BD51SMR");
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and complete Vehicle identification number section and click save and continue$", () -> {
            VehicleIdentificationNumberPage.untilOnIdentificationPage();
            VehicleIdentificationNumberPage.hasPageHeading();
            BasePermitPage.getReferenceFromPage();
            VehicleIdentificationNumberPage.identificationNumber();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and complete Vehicle Engine number section and click save and continue$", () -> {
            VehicleEngineNumberPage.untilOnEngineNumberPage();
            VehicleEngineNumberPage.hasPageHeading();
            BasePermitPage.getReferenceFromPage();
            VehicleEngineNumberPage.engineNumber();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and complete vehicle engine type section and click save and continue$", () -> {
            VehicleEngineTypePage.untilOnEngineTypePage();
            VehicleEngineTypePage.hasPageHeading();
            BasePermitPage.getReferenceFromPage();
            VehicleEngineTypePage.engineType();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and complete MOT DATE section and click save and continue$", () -> {
            VehicleMotPage.untilOnMotPage();
            VehicleMotPage.hasPageHeading();
            BasePermitPage.getReferenceFromPage();
            VehicleMotPage.motDate();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and click save and continue on the Check Your Answers page$", () -> {
            CheckYourAnswerPage.untilOnPage();
            CheckYourAnswerPage.hasPageHeading();
            CheckYourAnswerPage.saveAndContinue();
        });
        Then("^I check content and Accept and continue on the Declaration page$", () -> {
            DeclarationPage.untilOnPage();
            hasCheckBoxText();
            DeclarationPage.hasPageHeading();
            confirmDeclaration();
            DeclarationPage.saveAndContinue();
        });
        Then("^I check content of the Submitted page$", () -> {
            SubmittedPage.untilOnPage();
            SubmittedPage.hasPageHeading();
            SubmittedPage.hasCertificateAdvisoryText();
            SubmittedPage.hasWarningMessage();
        });
        Then("^I am navigated back to the permits dashboard page with my application status shown as Valid", () -> {
            String licence = operatorStore.getCurrentLicenceNumber().toString().substring(9, 18);
            untilAnyPermitStatusMatch(PermitStatus.VALID);
        });

    } //TODO Possible refactoring to be done here. See if it can be combined with trailers?
}

