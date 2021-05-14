package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java.hu.Ha;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.SectorPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitUsage;
import org.dvsa.testing.lib.pages.external.permit.enums.Sector;
import org.dvsa.testing.lib.pages.external.permit.enums.ShortTermApplicationInfo;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;
import org.hamcrest.core.StringContains;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.external.permit.enums.ShortTermApplicationInfo.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class CheckYourAnswersPageSteps implements En {

    public static Map<String, PermitUsage> permitUsage = new HashMap();

    public CheckYourAnswersPageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the Short term check your answers page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitTypePage.PermitType.ShortTermECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore).licencePage(operatorStore,world);
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);
            OverviewPage.select(OverviewPage.Section.HowwillyouusethePermits);
            licence.getEcmt().setPermitUsage(PermitUsage.random());
            PermitUsagePage.permitUsage(licence.getEcmt().getPermitusage());
            CheckYourAnswersPageSteps.permitUsage.put("permit.usage", licence.getEcmt().getPermitusage());
            BasePermitPage.saveAndContinue();
            CabotagePage.cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.CertificatesRequiredConfirmation();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage.euro5OrEuro6permitsValue();
            BasePermitPage.saveAndContinue();
            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();
            AnnualTripsAbroadPage.quantity(10);
            BasePermitPage.saveAndContinue();
            ProportionOfInternationalJourneyPage.proportion(JourneyProportion.LessThan60Percent);
            SectorPage.sector(Sector.random());
        });
        Then("^short term permit check your answers page has correct heading label$", () -> {
CheckYourAnswersPage.untilElementIsPresent("//h1[@class='govuk-heading-xl']", SelectorType.XPATH,10, TimeUnit.SECONDS);
                    CheckYourAnswersPage.hasPageHeading();
                });
        Then("^the short term check your answers page has reference number$", () -> {
            CheckYourAnswersPage.reference();
            System.out.println(CheckYourAnswersPage.reference());
        });
        Then("^Short term application answers are displayed on the check your answers page$", () -> {
            String licence = CheckYourAnswersPage.getAnswer(Licence);
            assertThat(licence, StringContains.containsString(operatorStore.getCurrentLicence().get().getLicenceNumber()));
            String cabotage = CheckYourAnswersPage.getAnswer(ShortTermApplicationInfo.Cabotage);
            Assert.assertEquals("I confirm that I will not undertake cabotage journeys using an ECMT permit.", cabotage);
            String certificates = CheckYourAnswersPage.getAnswer(CertificatesRequired);
            Assert.assertEquals("I understand that I must obtain and carry the appropriate Certificate of Compliance and Certificate of Roadworthiness for each vehicle and trailer I intend to use with this permit.", certificates);
            String permitType = CheckYourAnswersPage.getAnswer(PermitType);
            Assert.assertEquals("Short-term ECMT",permitType );
            String actualPermitUsage = CheckYourAnswersPage.getAnswer(PermitsUsage);
            String s = toShortTermPermitUsage(CheckYourAnswersPageSteps.permitUsage.get("permit.usage"));
            Assert.assertEquals(s,actualPermitUsage);
        });
        Then("^I am navigated to the short term declaration page$", DeclarationPage::declarationPageLoad);
        Then("^I am on the short term permits overview page with check your answers section marked as complete$", () -> {
            String error = "Expected the status of Declarations  page to be complete but it wasn't";
            OverviewPage.untilOnPage();
            boolean complete = OverviewPage.checkStatus(OverviewPage.Section.Checkyouranswers, PermitStatus.COMPLETED);
            Assert.assertTrue(error, complete);
        });
        Then("^the declaration section gets enabled to be clicked and section status changes to NOT STARTED YET$", CheckYourAnswersPage::decNotStartedYet);
        And("^I click Check your answers link on the overview page again$", () -> {
            OverviewPage.select(OverviewPage.Section.Checkyouranswers);
        });
        Then("^I am navigated to the short term check your answers page$", CheckYourAnswersPage::hasPageHeading);
    }

    private static String toShortTermPermitUsage(@NotNull PermitUsage permitUsage) {
        String result;

        switch (permitUsage) {
            case TRANSIT_ONLY:
                result = "Transit only";
                break;
            case CROSS_TRADE_ONLY:
                result = "Cross-trade only";
                break;
            case BOTH_TRANSIT_AND_CROSSTRADE:
                result = "Both transit and cross-trade";
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }
}