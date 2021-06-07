package org.dvsa.testing.framework.stepdefs.permits.TrailersCertificateOfRoadworthiness;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.TrailersCertificateOfRoadworthinessJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CheckYourAnswerPage;
import org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.permits.pages.SubmittedPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.trailersCertificateOfRoadworthiness.*;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage.confirmDeclaration;
import static org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage.hasCheckboxText;

public class TrailersCertificateOfRoadWorthinessE2E implements En {
    public TrailersCertificateOfRoadWorthinessE2E(OperatorStore operatorStore, World world) {
        LicenceStore licenceStore = operatorStore.getCurrentLicence().orElseGet(LicenceStore::new);
        And("^I select Certificate of Roadworthiness for trailers on the select permit page$", () -> {
            clickToPermitTypePage(world);
            TrailersCertificateOfRoadworthinessJourney.getInstance().permitType(PermitType.CERTIFICATE_OF_ROADWORTHINESS_FOR_TRAILERS,operatorStore);
        });

        Then("^I select any licence number for Certificate of Roadworthiness for trailers$", () -> {
            TrailersCertificateOfRoadworthinessJourney.getInstance().licencePage(operatorStore,world);

        });

        Then("^I am on Certificate of Roadworthiness for trailers Application overview Page$", org.dvsa.testing.lib.newPages.permits.pages.OverviewPage::untilOnPage);

        Then("^I check content and complete Registration number section for  Certificate of Roadworthiness for trailers and click save and continue$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.RegistrationNumber);
            VehicleRegistrationNumberPage.untilOnRegistrationPage();
            VehicleRegistrationNumberPage.hasPageHeading();
            // Check the new validation
            BasePermitPage.saveAndContinue();
            VehicleRegistrationNumberPage.hasRequiredFieldValidation();
            BasePermitPage.getReferenceFromPage();
            VehicleRegistrationNumberPage.registrationNumber();

            BasePermitPage.saveAndContinue();
        });

        Then("^I check content and complete Certificate  of Compliance section Certificate of Roadworthiness for trailers and click save and continue$", () -> {
            CertificateOfComplianceNumberPage.untilOnCertificatePage();
            CertificateOfComplianceNumberPage.hasPageHeading();
            BasePermitPage.getReferenceFromPage();
            CertificateOfComplianceNumberPage.ComplianceNumber();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and complete vehicle make and model section Certificate of Roadworthiness for trailers and click save and continue$", () -> {
            MakeAndModelPage.untilOnMakeAndModelPage();
            MakeAndModelPage.hasPageHeading();
            BasePermitPage.getReferenceFromPage();
            MakeAndModelPage.MakeAndModel();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and complete Vehicle identification number section Certificate of Roadworthiness for trailers and click save and continue$", () -> {
            VehicleIdentificationNumberPage.untilOnIdentificationPage();
            VehicleIdentificationNumberPage.hasPageHeading();
            BasePermitPage.getReferenceFromPage();
            VehicleIdentificationNumberPage.identificationNumber();
            BasePermitPage.saveAndContinue();
        });

        Then("^I check content and complete MOT DATE section Certificate of Roadworthiness for trailers and click save and continue$", () -> {
            VehicleMotPage.untilOnMotPage();
            VehicleMotPage.hasPageHeading();
            BasePermitPage.getReferenceFromPage();
            VehicleMotPage.motDate();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and click save and continue on the Check Your Answers page for Certificate of Roadworthiness for trailers$", () -> {
            CheckYourAnswerPage.untilOnPage();
            CheckYourAnswerPage.hasPageHeading();
            CheckYourAnswerPage.saveAndContinue();
        });
        Then("^I check content and Accept and continue on the Declaration page for Certificate of Roadworthiness for trailers page$", () -> {
            DeclarationPage.untilOnPage();
            DeclarationPage.hasTrailersCertificateAdvisoryMessages();
            DeclarationPage.hasPageHeading();
            hasCheckboxText();
            confirmDeclaration();
            DeclarationPage.saveAndContinue();
        });
        Then("^I check content of the Submitted page for Certificate of Roadworthiness for trailers$", () -> {
            SubmittedPage.untilOnPage();
            SubmittedPage.hasPageHeading();
            SubmittedPage.hasCertificateAdvisoryText();
            SubmittedPage.hasWarningMessage();
        });
        Then("^I am navigated back to the permits dashboard page for Certificate of Roadworthiness for trailers with my application status shown as Valid", () -> {
            String licence = operatorStore.getCurrentLicenceNumber().toString().substring(9, 18);
            untilAnyPermitStatusMatch(PermitStatus.VALID);
        });

    }
}
