package org.dvsa.testing.framework.Journeys.permits;

import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.*;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.ECMTPermitApplicationSteps;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;


public class EcmtApplicationJourney extends BasePermitJourney {

    protected EcmtApplicationJourney() { }

    public static void completeEcmtApplication(World world) {
        completeEcmtApplicationConfirmation(world);
        SubmittedPage.untilPageLoad();
        SubmittedPage.goToPermitsDashboard();
    }

    public static void completeEcmtApplicationConfirmation(World world) {
        completeApplicationUntilFeePage(world);
        PermitFeePage.saveAndContinue();
        world.feeAndPaymentJourney.customerPaymentModule();
    }

    public static void completeApplicationUntilFeePage(World world){
        beginApplication(world);
        EcmtApplicationJourney.completeUntilCheckYourAnswersPage();
        ECMTPermitApplicationSteps.saveAndContinue();
        DeclarationPageJourney.completeDeclaration();
    }

    public static void completeUntilCheckYourAnswersPage() {
        OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
        CheckIfYouNeedECMTPermitsPageJourney.completePage();
        CabotagePageJourney.completePage();
        CertificatesRequiredPageJourney.completePage();
        CountriesWithLimitedPermitsPage.chooseNoCountriesWithLimitedPermits();
        NumberOfPermitsPageJourney.completeECMTPage();
        EmissionStandardsPageJourney.completePage();
        BasePermitJourney.setFullReferenceNumber(BasePermitPage.getReferenceFromPage());
    }

    public static void completeUntilNumberOfPermitsPage(World world) {
        completeUntilRestrictedCountriesPage(world);
        CountriesWithLimitedPermitsPage.chooseNoCountriesWithLimitedPermits();
    }

    public static void completeUntilRestrictedCountriesPage(World world){
        completeUntilCertificatesRequiredPage(world);
        CertificatesRequiredPageJourney.completePage();
    }

    public static void completeUntilCertificatesRequiredPage(World world){
        completeUntilCabotagePage(world);
        CabotagePageJourney.completePage();
    }

    public static void completeUntilCabotagePage(World world) {
        beginApplication(world);
        OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
        CheckIfYouNeedECMTPermitsPageJourney.completePage();
    }

    public static void beginApplication(World world) {
        beginApplicationToLicenceSelectionPage(world);
        BasePermitJourney.licencePage(world);
        OverviewPage.untilOnPage();
    }

    public static void beginApplicationToLicenceSelectionPage(World world) {
        beginApplicationToYearSelectionPage(world);
        YearSelectionPage.selectECMTValidityPeriod();
    }

    public static void beginApplicationToYearSelectionPage(World world) {
        CommonSteps.clickToPermitTypePage(world);
        BasePermitJourney.permitType(PermitType.ECMT_ANNUAL);
    }
}