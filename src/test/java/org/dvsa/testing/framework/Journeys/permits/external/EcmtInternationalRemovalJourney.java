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

    public static void completeApplicationUntilRemovalEligibilityPage(World world) {
        beginApplication(world);
        OverviewPageJourney.clickOverviewSection(OverviewSection.RemovalsEligibility);
    }

    public static void completeApplicationUntilCabotagePage(World world) {
        completeApplicationUntilRemovalEligibilityPage(world);
        RemovalsEligibilityPageJourney.completePage();
    }

    public static void completeApplicationUntilCertificatesRequiredPage(World world) {
        completeApplicationUntilCabotagePage(world);
        CabotagePageJourney.completePage();
    }

    public static void completeApplicationUntilPermitStartDatePage(World world) {
        completeApplicationUntilCertificatesRequiredPage(world);
        CertificatesRequiredPageJourney.completePage();
    }

    public static void completeApplicationUntilNumberOfPermitsPage(World world) {
        completeApplicationUntilPermitStartDatePage(world);
        PermitStartDatePageJourney.completePage();
    }

    public static void completeApplicationUntilCheckYourAnswersPage(World world) {
        completeApplicationUntilNumberOfPermitsPage(world);
        NumberOfPermitsPageJourney.completePage();
    }

    public static void completeApplicationUntilDeclarationPage(World world) {
        completeApplicationUntilCheckYourAnswersPage(world);
        CheckYourAnswersPageJourney.completePage();
    }

    public static void completeAndSubmitApplication(World world) {
        completeApplicationUntilDeclarationPage(world);
        DeclarationPageJourney.completeDeclaration();
        PermitFeePage.saveAndContinue();
        world.feeAndPaymentJourney.customerPaymentModule();
        SubmittedPage.untilOnPage();
    }


}