package org.dvsa.testing.framework.Journeys.permits;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.*;
import org.dvsa.testing.framework.Utils.Generic.EnhancedWaitUtils;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly.PermitStartDatePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly.RemovalsEligibilityPage;


public class EcmtInternationalRemovalJourney extends BasePermitJourney {

    World world;

    public EcmtInternationalRemovalJourney(World world) {
        super(world);
        this.world = world;
    }


    public void beginApplication() {
        clickToPermitTypePage();
        world.basePermitJourney.permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL);
        world.basePermitJourney.licencePage();
        OverviewPage.untilOnPage();
        EnhancedWaitUtils.waitForPageReady();
    }


    public void completeUntilRemovalEligibilityPage() {
        beginApplication();

        OverviewPage.untilOnPage();
        EnhancedWaitUtils.waitForPageReady();
        OverviewPageJourney.clickOverviewSection(OverviewSection.RemovalsEligibility);
        RemovalsEligibilityPage.untilOnPage();
        EnhancedWaitUtils.waitForPageReady();
    }


    public void completeUntilCabotagePage() {
        beginApplication();

        OverviewPageJourney.clickOverviewSection(OverviewSection.RemovalsEligibility);
        RemovalsEligibilityPage.untilOnPage();
        EnhancedWaitUtils.waitForPageReady();
        RemovalsEligibilityPageJourney.completePage();


        EnhancedWaitUtils.waitForPageReady();
    }


    public void completeUntilCertificatesRequiredPage() {
        completeUntilCabotagePage();

        CabotagePageJourney.completePage();

        CertificatesRequiredPage.untilOnPage();
        EnhancedWaitUtils.waitForPageReady();
    }


    public void completeUntilPermitStartDatePage() {
        completeUntilCertificatesRequiredPage();

        CertificatesRequiredPageJourney.completePage();

        PermitStartDatePage.untilOnPage();
        EnhancedWaitUtils.waitForPageReady();
    }


    public void completeUntilNumberOfPermitsPage() {
        completeUntilPermitStartDatePage();

        PermitStartDatePageJourney.completePage();

        NumberOfPermitsPage.untilOnPage();
        EnhancedWaitUtils.waitForPageReady();
    }


    public void completeUntilCheckYourAnswersPage() {
        completeUntilNumberOfPermitsPage();
        NumberOfPermitsPageJourney.completePage();

        CheckYourAnswerPage.untilOnPage();
        EnhancedWaitUtils.waitForPageReady();
    }


    public void completeUntilDeclarationPage() {
        completeUntilCheckYourAnswersPage();
        CheckYourAnswersPageJourney.completePage();

        DeclarationPage.untilOnPage();
        EnhancedWaitUtils.waitForPageReady();
    }


    public void completeAndSubmitApplication() {
        completeUntilDeclarationPage();
        DeclarationPageJourney.completeDeclaration();

        PermitFeePage.untilOnPage();
        EnhancedWaitUtils.waitForPageReady();
        PermitFeePage.saveAndContinue();

        world.feeAndPaymentJourney.customerPaymentModule();

        SubmittedPage.untilOnPage();
        EnhancedWaitUtils.waitForPageReady();
    }
}