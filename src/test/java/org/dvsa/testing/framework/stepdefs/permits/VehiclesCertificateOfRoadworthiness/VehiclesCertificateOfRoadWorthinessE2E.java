package org.dvsa.testing.framework.stepdefs.permits.VehiclesCertificateOfRoadworthiness;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.VehiclesCertificateOfRoadworthinessJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.SubmittedPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckYourAnswerPage;
import org.dvsa.testing.framework.pageObjects.external.pages.DeclarationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness.*;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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


        Then("^I check content and complete Registration number section and click save and continue$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.RegistrationNumber);
            VehicleRegistrationNumberPage.untilOnPage();
            assertEquals("Enter the vehicle registration number", VehicleRegistrationNumberPage.getPageHeading());
            BasePermitPage.getReferenceFromPage();
            VehicleRegistrationNumberPage.enterRegistrationNumber();
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
            VehicleIdentificationNumberPage.untilOnPage();
            assertEquals("Enter the vehicle identification number (VIN)", VehicleIdentificationNumberPage.getPageHeading());
            BasePermitPage.getReferenceFromPage();
            VehicleIdentificationNumberPage.enterIdentificationNumber();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and complete Vehicle Engine number section and click save and continue$", () -> {
            VehicleEngineNumberPage.untilOnPage();
            assertEquals("Enter the vehicle engine number", VehicleEngineNumberPage.getPageHeading());
            BasePermitPage.getReferenceFromPage();
            VehicleEngineNumberPage.enterEngineNumber();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and complete vehicle engine type section and click save and continue$", () -> {
            VehicleEngineTypePage.untilOnPage();
            assertEquals("Enter the vehicle engine type", VehicleEngineTypePage.getPageHeading());
            BasePermitPage.getReferenceFromPage();
            VehicleEngineTypePage.enterEngineType();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and complete MOT DATE section and click save and continue$", () -> {
            VehicleMotPage.untilOnPage();
            assertEquals("Enter the vehicle's MOT expiry date", VehicleMotPage.getPageHeading());
            BasePermitPage.getReferenceFromPage();
            VehicleMotPage.enterMOTDate();
            BasePermitPage.saveAndContinue();
        });
        Then("^I check content and click save and continue on the Check Your Answers page$", () -> {
            CheckYourAnswerPage.untilOnPage();
            String heading = CheckYourAnswerPage.getPageHeading();
            assertEquals("Check your answers", heading);
            CheckYourAnswerPage.saveAndContinue();
        });
        Then("^I check content and Accept and continue on the Declaration page$", () -> {
            DeclarationPage.untilOnPage();
            DeclarationPageJourney.hasPageHeading();
            DeclarationPageJourney.hasCheckboxText();
            DeclarationPageJourney.completeDeclaration();
        });
        Then("^I check content of the Submitted page$", () -> {
            SubmittedPage.untilOnPage();
            SubmittedPageJourney.hasPageHeading();
            SubmittedPageJourney.hasSubHeading();
            assertTrue(SubmittedPage.isCertificateAdvisoryTextPresent());
            assertTrue(SubmittedPage.isWarningMessagePresent());
        });
        Then("^I am navigated back to the permits dashboard page with my application status shown as Valid", () -> {
            untilAnyPermitStatusMatch(PermitStatus.VALID);
        });

    } //TODO Possible refactoring to be done here. See if it can be combined with trailers?
}

