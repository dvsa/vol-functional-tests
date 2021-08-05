package org.dvsa.testing.framework.Journeys.permits.external;


import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class EcmtInternationalRemovalJourney extends BasePermitJourney {

    public static void beginApplication(World world) {
        clickToPermitTypePage(world);
        BasePermitJourney.permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL);
        BasePermitJourney.licencePage(world);
    }

    public static void completeUntilRemovalEligibilityPage(World world) {
        beginApplication(world);
        OverviewPageJourney.clickOverviewSection(OverviewSection.RemovalsEligibility);
    }

    public static void completeUntilCabotagePage(World world) {
        completeUntilRemovalEligibilityPage(world);
        RemovalsEligibilityPageJourney.completePage();
    }

    public static void completeUntilCertificatesRequiredPage(World world) {
        completeUntilCabotagePage(world);
        CabotagePageJourney.completePage();
    }

    public static void completeUntilPermitStartDatePage(World world) {
        completeUntilCertificatesRequiredPage(world);
        CertificatesRequiredPageJourney.completePage();
    }

    public static void completeUntilNumberOfPermitsPage(World world) {
        completeUntilPermitStartDatePage(world);
        PermitStartDatePageJourney.completePage();
    }

    public static void completeUntilCheckYourAnswersPage(World world) {
        completeUntilNumberOfPermitsPage(world);
        NumberOfPermitsPageJourney.completePage();
    }

    public static void completeUntilDeclarationPage(World world) {
        completeUntilCheckYourAnswersPage(world);
        CheckYourAnswersPageJourney.completePage();
    }

    public static void completeAndSubmitApplication(World world) {
        completeUntilDeclarationPage(world);
        DeclarationPageJourney.completeDeclaration();
        PermitFeePage.saveAndContinue();
        world.feeAndPaymentJourney.customerPaymentModule();
        SubmittedPage.untilOnPage();
    }


}