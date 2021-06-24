package org.dvsa.testing.framework.stepdefs.permits.TrailersCertificateOfRoadworthiness;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.TrailersCertificateOfRoadworthinessJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.SubmittedPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.pages.DeclarationPage;
import org.dvsa.testing.lib.newPages.external.pages.SubmittedPage;
import org.dvsa.testing.lib.newPages.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness.CertificateOfComplianceNumberPage;
import org.dvsa.testing.lib.newPages.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness.MakeAndModelPage;
import org.dvsa.testing.lib.newPages.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness.VehicleIdentificationNumberPage;
import org.dvsa.testing.lib.newPages.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness.VehicleMotPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.trailersCertificateOfRoadworthiness.*;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        Then("^I check content and complete Registration number section for  Certificate of Roadworthiness for trailers and click save and continue$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.RegistrationNumber);
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
            CertificateOfComplianceNumberPage.untilOnPage();
            String heading = CertificateOfComplianceNumberPage.getPageHeading();
            assertEquals("Enter the trailer Certificate of Compliance number (optional)", heading);
            BasePermitPage.getReferenceFromPage();
            CertificateOfComplianceNumberPage.enterComplianceNumber("BD51SMR");
            CertificateOfComplianceNumberPage.saveAndContinue();
        });
        Then("^I check content and complete vehicle make and model section Certificate of Roadworthiness for trailers and click save and continue$", () -> {
            MakeAndModelPage.untilOnPage();
            String heading = MakeAndModelPage.getPageHeading();
            assertEquals("Enter the trailer make and model", heading);
            BasePermitPage.getReferenceFromPage();
            MakeAndModelPage.enterMakeAndModel("BD51SMR");
            MakeAndModelPage.saveAndContinue();
        });
        Then("^I check content and complete Vehicle identification number section Certificate of Roadworthiness for trailers and click save and continue$", () -> {
            VehicleIdentificationNumberPage.untilOnIdentificationPage();
            assertEquals("Enter the trailer's vehicle identification number (VIN)", VehicleIdentificationNumberPage.getPageHeading());
            BasePermitPage.getReferenceFromPage();
            VehicleIdentificationNumberPage.enterIdentificationNumber();
            BasePermitPage.saveAndContinue();
        });

        Then("^I check content and complete MOT DATE section Certificate of Roadworthiness for trailers and click save and continue$", () -> {
            VehicleMotPage.untilOnMotPage();
            assertEquals("Enter the trailer's vehicle MOT expiry date", VehicleMotPage.getPageHeading());
            BasePermitPage.getReferenceFromPage();
            VehicleMotPage.motDate();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and Accept and continue on the Declaration page for Certificate of Roadworthiness for trailers page$", () -> {
            DeclarationPage.untilOnPage();
            DeclarationPageJourney.hasPageHeading();
            assertTrue(DeclarationPage.isTrailersCertificateAdvisoryMessagePresent());
            DeclarationPageJourney.hasCheckboxText();
            DeclarationPageJourney.completeDeclaration();
        });
        Then("^I check content of the Submitted page for Certificate of Roadworthiness for trailers$", () -> {
            SubmittedPage.untilOnPage();
            SubmittedPageJourney.hasPageHeading();
            SubmittedPageJourney.hasSubHeading();
            assertTrue(SubmittedPage.isCertificateAdvisoryTextPresent());
            assertTrue(SubmittedPage.isWarningMessagePresent());
        });
        Then("^I am navigated back to the permits dashboard page for Certificate of Roadworthiness for trailers with my application status shown as Valid", () -> {
            String licence = operatorStore.getCurrentLicenceNumber().toString().substring(9, 18);
            untilAnyPermitStatusMatch(PermitStatus.VALID);
        });

    }
}
