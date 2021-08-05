package org.dvsa.testing.framework.Journeys.permits;

import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.*;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PeriodSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;

public class ShortTermECMTJourney extends BasePermitJourney {

    public ShortTermECMTJourney() { }

    public static void beginApplication(World world) {
        CommonSteps.clickToPermitTypePage(world);
        BasePermitJourney.permitType(PermitType.SHORT_TERM_ECMT);
        YearSelectionPage.selectShortTermValidityPeriod();
        PeriodSelectionPage.selectFirstAvailablePermitPeriod();
        PeriodSelectionPage.saveAndContinue();
        BasePermitJourney.licencePage(world);
    }

    public static void completeShortTermECMTApplication(World world) {
        ShortTermECMTJourney.beginApplication(world);
        OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
        CheckIfYouNeedECMTPermitsPage.saveAndContinue();
        CheckIfYouNeedECMTPermitsPage.hasErrorMessagePresent();
        CheckIfYouNeedECMTPermitsPageJourney.completePage();
        CabotagePageJourney.completePage();
        CertificatesRequiredPageJourney.completePage();
        CountriesWithLimitedPermitsPage.chooseNoCountriesWithLimitedPermits();
        NumberOfPermitsPageJourney.completeECMTPage();
        PermitStartDatePageJourney.completePage();
        EmissionStandardsPageJourney.completePage();
        saveAndContinue();
        DeclarationPageJourney.completeDeclaration();
        PermitFeePage.submitAndPay();
        world.feeAndPaymentJourney.customerPaymentModule();
        SubmittedPage.untilOnPage();
        SubmittedPage.goToPermitsDashboard();
    }
}
