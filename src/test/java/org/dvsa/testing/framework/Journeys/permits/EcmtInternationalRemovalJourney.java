package org.dvsa.testing.framework.Journeys.permits;


import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.*;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;

public class EcmtInternationalRemovalJourney extends BasePermitJourney {

    World world;
    public EcmtInternationalRemovalJourney(World world){
        super(world);
        this.world = world;
    }
    public void beginApplication() {
        clickToPermitTypePage();
        world.basePermitJourney.permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL);
        world.basePermitJourney.licencePage();
    }

    public void completeUntilRemovalEligibilityPage() {
        beginApplication();
        OverviewPageJourney.clickOverviewSection(OverviewSection.RemovalsEligibility);
    }

    public void completeUntilCabotagePage() {
        completeUntilRemovalEligibilityPage();
        RemovalsEligibilityPageJourney.completePage();
    }

    public void completeUntilCertificatesRequiredPage() {
        completeUntilCabotagePage();
        CabotagePageJourney.completePage();
    }

    public void completeUntilPermitStartDatePage() {
        completeUntilCertificatesRequiredPage();
        CertificatesRequiredPageJourney.completePage();
    }

    public void completeUntilNumberOfPermitsPage() {
        completeUntilPermitStartDatePage();
        PermitStartDatePageJourney.completePage();
    }

    public void completeUntilCheckYourAnswersPage() {
        completeUntilNumberOfPermitsPage();
        NumberOfPermitsPageJourney.completePage();
    }

    public void completeUntilDeclarationPage() {
        completeUntilCheckYourAnswersPage();
        CheckYourAnswersPageJourney.completePage();
    }

    public void completeAndSubmitApplication() {
        completeUntilDeclarationPage();
        DeclarationPageJourney.completeDeclaration();
        PermitFeePage.saveAndContinue();
        world.feeAndPaymentJourney.customerPaymentModule();
        SubmittedPage.untilOnPage();
    }
}