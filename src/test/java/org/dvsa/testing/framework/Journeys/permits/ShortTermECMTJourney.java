package org.dvsa.testing.framework.Journeys.permits;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.*;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PeriodSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;

public class ShortTermECMTJourney extends BasePermitJourney {

    public ShortTermECMTJourney(World world) {
        super(world);
    }

    public void beginApplication() {
        world.basePermitJourney.clickToPermitTypePage();
        world.basePermitJourney.permitType(PermitType.SHORT_TERM_ECMT);
        world.yearSelectionPage.selectShortTermValidityPeriod();
        PeriodSelectionPage.selectFirstAvailablePermitPeriod();
        PeriodSelectionPage.saveAndContinue();
        world.basePermitJourney.licencePage();
    }

    public void completeShortTermECMTApplication() {
        beginApplication();
        OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
        CheckIfYouNeedECMTPermitsPage.saveAndContinue();
        CheckIfYouNeedECMTPermitsPage.hasErrorMessagePresent();
        CheckIfYouNeedECMTPermitsPageJourney.completePage();
        CabotagePageJourney.completePage();
        CertificatesRequiredPageJourney.completePage();
        world.countriesWithLimitedPermitsPage.chooseNoCountriesWithLimitedPermits();
        NumberOfPermitsPageJourney.completeECMTPage();
        PermitStartDatePageJourney.completePage();
        EmissionStandardsPageJourney.completePage();
        saveAndContinue();
        DeclarationPageJourney.completeDeclaration();
        world.permitFeePage.submitAndPay();
        world.feeAndPaymentJourney.customerPaymentModule();
        SubmittedPage.untilOnPage();
        SubmittedPage.goToPermitsDashboard();
    }
}
