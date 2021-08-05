package org.dvsa.testing.framework.Journeys.permits.external;

import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.enums.JourneyType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class AnnualBilateralJourney extends BasePermitJourney {

    private static PeriodType periodType;

    protected AnnualBilateralJourney() { }

    public static void setPeriodType(PeriodType periodType) {
        AnnualBilateralJourney.periodType = periodType;
    }

    public static PeriodType getPeriodType() {
        return periodType;
    }

    public static void completePeriodTypePage(PeriodType bilateralPeriodType) {
        AnnualBilateralJourney.setPeriodType(bilateralPeriodType);
        PeriodSelectionPage.chooseBilateralPeriodType(bilateralPeriodType);
        PeriodSelectionPage.saveAndContinue();
    }

    public static void startBilateralJourneyTypeAndSelectCabotageUntilPermitFeePage(World world, PeriodType bilateralType, Country country, Boolean cabotageChoice) {
        startBilateralJourneyTypeAndSelectCabotageUntilCheckYourAnswersPage(world, bilateralType, country, cabotageChoice);
        BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
        OverviewPageJourney.clickOverviewSection(OverviewSection.BilateralDeclaration);
        DeclarationPageJourney.completeDeclaration();
    }

    public static void startBilateralJourneyTypeAndSelectCabotageUntilCheckYourAnswersPage(World world, PeriodType bilateralType, Country country, Boolean cabotageChoice) {
        startBilateralCountryJourneyAndSelectCountry(world, country.toString());
        OverviewPage.clickCountrySection(country);
        EssentialInformationPageJourney.completePage();
        AnnualBilateralJourney.completePeriodTypePage(bilateralType);
        PermitUsagePageJourney.completePermitTypePage(JourneyType.MultipleJourneys);
        if (bilateralType != PeriodType.BilateralsStandardPermitsNoCabotage) {
            completeCabotage(cabotageChoice);
        }
        NumberOfPermitsPageJourney.completePage();
    }

    public static void startBilateralCountryJourneyAndSelectCountry(World world, String country) {
        clickToPermitTypePage(world);
        BasePermitJourney.permitType(PermitType.ANNUAL_BILATERAL);
        BasePermitJourney.licencePage(world);
        CountrySelectionPage.selectCountry(country);
        OverviewPage.untilOnPage();
    }

    public static void completeCabotage(boolean cabotageChoice) {
        if (cabotageChoice) {
            CabotagePage.clickNoToCabotage();
        } else {
            CabotagePage.clickYesToCabotage();
            CabotagePage.yesAndCabotagePermitConfirmation();
        }
        CabotagePage.saveAndContinue();
    }

    public static void completePayFees(World world) {
        PermitFeePage.untilOnPage();
        PermitFeePage.submitAndPay();
        world.feeAndPaymentJourney.customerPaymentModule();
    }

}
