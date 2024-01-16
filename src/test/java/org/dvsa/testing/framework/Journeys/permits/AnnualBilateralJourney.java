package org.dvsa.testing.framework.Journeys.permits;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.*;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;


public class AnnualBilateralJourney extends BasePermitJourney {

    private static PeriodType periodType;

    public AnnualBilateralJourney(World world) {
        super(world);
    }

    public void setPeriodType(PeriodType periodType) {
        AnnualBilateralJourney.periodType = periodType;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public void completePeriodTypePage(PeriodType bilateralPeriodType) {
        world.annualBilateralJourney.setPeriodType(bilateralPeriodType);
        PeriodSelectionPage.chooseBilateralPeriodType(bilateralPeriodType);
        PeriodSelectionPage.saveAndContinue();
    }

    public  void startBilateralJourneyTypeAndSelectCabotageUntilPermitFeePage(PeriodType bilateralType, Country country, Boolean cabotageChoice) {
        startBilateralJourneyTypeAndSelectCabotageUntilCheckYourAnswersPage(bilateralType, country, cabotageChoice);
        BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
        OverviewPageJourney.clickOverviewSection(OverviewSection.BilateralDeclaration);
        DeclarationPageJourney.completeDeclaration();
    }

    public void startBilateralJourneyTypeAndSelectCabotageUntilCheckYourAnswersPage(PeriodType bilateralType, Country country, Boolean cabotageChoice) {
        startBilateralCountryJourneyAndSelectCountry(country.toString());
        OverviewPage.clickCountrySection(country);
        EssentialInformationPageJourney.completePage();
        world.annualBilateralJourney.completePeriodTypePage(bilateralType);
        PermitUsagePageJourney.completePermitTypePage(JourneyType.MultipleJourneys);
        if (bilateralType != PeriodType.BilateralsStandardPermitsNoCabotage) {
            completeCabotage(cabotageChoice);
        }
        NumberOfPermitsPageJourney.completePage();
    }

    public void startBilateralCountryJourneyAndSelectCountry(String country) {
        clickToPermitTypePage();
        world.basePermitJourney.permitType(PermitType.ANNUAL_BILATERAL);
        world.basePermitJourney.licencePage();
        CountrySelectionPage.selectCountry(country);
        OverviewPage.untilOnPage();
    }

    public void completeCabotage(boolean cabotageChoice) {
        if (cabotageChoice) {
            CabotagePage.clickNoToCabotage();
        } else {
            CabotagePage.clickYesToCabotage();
            CabotagePage.yesAndCabotagePermitConfirmation();
        }
        CabotagePage.saveAndContinue();
    }

    public void completePayFees() {
        world.permitFeePage.untilOnPage();
        world.permitFeePage.submitAndPay();
        world.feeAndPaymentJourney.customerPaymentModule();
    }
}
