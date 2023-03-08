package org.dvsa.testing.framework.Journeys.permits;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.*;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.stepdefs.permits.annualecmt.ECMTPermitApplicationSteps;


public class EcmtApplicationJourney extends BasePermitJourney {

    public EcmtApplicationJourney(World world) {
        super(world);
    }

    public void completeEcmtApplication() {
        completeEcmtApplicationConfirmation();
        SubmittedPage.untilPageLoad();
        SubmittedPage.goToPermitsDashboard();
    }

    public void completeEcmtApplicationConfirmation() {
        completeApplicationUntilFeePage();
        PermitFeePage.saveAndContinue();
        world.feeAndPaymentJourney.customerPaymentModule();
    }

    public void completeApplicationUntilFeePage(){
        beginApplication();
        completeUntilCheckYourAnswersPage();
        ECMTPermitApplicationSteps.saveAndContinue();
        DeclarationPageJourney.completeDeclaration();
    }

    public void completeUntilCheckYourAnswersPage() {
        OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
        CheckIfYouNeedECMTPermitsPageJourney.completePage();
        CabotagePageJourney.completePage();
        CertificatesRequiredPageJourney.completePage();
        world.countriesWithLimitedPermitsPage.chooseNoCountriesWithLimitedPermits();
        NumberOfPermitsPageJourney.completeECMTPage();
        EmissionStandardsPageJourney.completePage();
        world.basePermitJourney.setFullReferenceNumber(BasePermitPage.getReferenceFromPage());
    }

    public void completeUntilNumberOfPermitsPage() {
        completeUntilRestrictedCountriesPage();
        world.countriesWithLimitedPermitsPage.chooseNoCountriesWithLimitedPermits();
    }

    public void completeUntilRestrictedCountriesPage(){
        completeUntilCertificatesRequiredPage();
        CertificatesRequiredPageJourney.completePage();
    }

    public void completeUntilCertificatesRequiredPage(){
        completeUntilCabotagePage();
        CabotagePageJourney.completePage();
    }

    public void completeUntilCabotagePage() {
        beginApplication();
        OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
        CheckIfYouNeedECMTPermitsPageJourney.completePage();
    }

    public void beginApplication() {
        beginApplicationToLicenceSelectionPage();
        world.basePermitJourney.licencePage();
        OverviewPage.untilOnPage();
    }

    public void beginApplicationToLicenceSelectionPage() {
        beginApplicationToYearSelectionPage();
        world.yearSelectionPage.selectECMTValidityPeriod();
    }

    public void beginApplicationToYearSelectionPage() {
        world.basePermitJourney.clickToPermitTypePage();
        world.basePermitJourney.permitType(PermitType.ECMT_ANNUAL);
    }
}