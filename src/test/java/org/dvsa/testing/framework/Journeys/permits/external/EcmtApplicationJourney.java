package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;


public class EcmtApplicationJourney extends BasePermitJourney {

    protected EcmtApplicationJourney() { }

    public static void completeEcmtApplication(World world) {
        completeEcmtApplicationConfirmation(world);
        SubmittedPage.untilPageLoad();
        SubmittedPage.goToPermitsDashboard();
    }

    public static void completeEcmtApplicationConfirmation(World world) {
        BasePermitJourney.permitType(PermitType.ECMT_ANNUAL);
        YearSelectionPage.selectECMTValidityPeriod();
        BasePermitJourney.licencePage(world);
        completeUpToCheckYourAnswersPage();
        CheckYourAnswerPage.untilOnPage();
        CheckYourAnswerPage.saveAndContinue();
        DeclarationPageJourney.completeDeclaration();
        PermitFeePage.saveAndContinue();
        world.feeAndPaymentJourney.customerPaymentModule();
    }

    public static void completeUpToCheckYourAnswersPage() {
        OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
        CheckIfYouNeedECMTPermitsPageJourney.completePage();
        CabotagePage.confirmWontUndertakeCabotage();
        CabotagePage.saveAndContinue();
        CertificatesRequiredPageJourney.completePage();
        CountriesWithLimitedPermitsPage.chooseNoCountriesWithLimitedPermits();
        NumberOfPermitsPageJourney.completeECMTPage();
        EmissionStandardsPageJourney.completePage();
        BasePermitJourney.setReferenceNumber(BasePermitPage.getReferenceFromPage());
    }
}