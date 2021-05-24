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
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.DeclarationPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.vehiclesCertificateOfRoadworthiness.*;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.external.permit.vehiclesCertificateOfRoadworthiness.DeclarationPage.*;


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

        Then("^I am on Certificate of Roadworthiness for vehicles Application overview Page$", OverviewPage::untilOnOverviewPage);

        Then("^I check content and complete Registration number section and click save and continue$", () -> {
            OverviewPage.select(OverviewSection.RegistrationNumber);
            VehicleRegistrationNumberPage.untilOnRegistrationPage();
            VehicleRegistrationNumberPage.hasPageHeading();
            BasePermitPage.getReferenceFromPage();
            VehicleRegistrationNumberPage.registrationNumber();
            BasePermitPage.saveAndContinue();
        });

        Then("^I check content and complete Certificate  of Compliance section and click save and continue$", () -> {
            CertificateOfComplianceNumberPage.untilOnCertificatePage();
            CertificateOfComplianceNumberPage.hasPageHeading();
            BasePermitPage.getReferenceFromPage();
            CertificateOfComplianceNumberPage.ComplianceNumber();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and complete vehicle make and model section and click save and continue$", () -> {
            MakeAndModelPage.untilOnMakeAndModelPage();
            MakeAndModelPage.hasPageHeading();
            BasePermitPage.getReferenceFromPage();
            MakeAndModelPage.MakeAndModel();
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
            CheckYourAnswersPage.checkAnswersPageLoad();
            CheckYourAnswerPage.hasPageHeading();
            CheckYourAnswerPage.saveAndContinue();
        });
        Then("^I check content and Accept and continue on the Declaration page$", () -> {

            declarationPageLoad();
            hasCheckbox();
            hasPageHeading();
            DeclarationConfirmation();
            DeclarationPage.saveAndContinue();
        });
        Then("^I check content of the Submitted page$", () -> {
            SubmittedPage.SubmissionPageLoad();
            SubmittedPage.pageHeading();
            SubmittedPage.advisoryText();
            SubmittedPage.warningMessage();

        });
        Then("^I am navigated back to the permits dashboard page with my application status shown as Valid", () -> {
            String licence = operatorStore.getCurrentLicenceNumber().toString().substring(9, 18);
            untilAnyPermitStatusMatch(PermitStatus.VALID);
        });

    } //TODO Possible refactoring to be done here
}

