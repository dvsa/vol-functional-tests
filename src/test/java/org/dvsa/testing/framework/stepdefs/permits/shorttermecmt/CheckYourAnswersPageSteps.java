package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.SectorPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.Sector;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CabotagePage;
import org.hamcrest.core.StringContains;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.external.permit.enums.sections.ShortTermApplicationSection.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class CheckYourAnswersPageSteps implements En {

    public static Map<String, PermitUsage> permitUsage = new HashMap();

    public CheckYourAnswersPageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the Short term check your answers page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(org.dvsa.testing.lib.enums.PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore).licencePage(operatorStore,world);
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            licence.getEcmt().setPermitUsage(PermitUsage.random());
            PermitUsagePage.permitUsage(licence.getEcmt().getPermitusage());
            CheckYourAnswersPageSteps.permitUsage.put("permit.usage", licence.getEcmt().getPermitusage());
            BasePermitPage.saveAndContinue();
            CabotagePage.cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.confirmCertificateRequired();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            NumberOfPermitsPage.enterEuro5OrEuro6permitsValue();
            BasePermitPage.saveAndContinue();
            EmissionStandardsPage.confirmCheckbox();
            BasePermitPage.saveAndContinue();
            AnnualTripsAbroadPage.quantity(10);
            BasePermitPage.saveAndContinue();
            ProportionOfInternationalJourneyPage.proportion(JourneyProportion.LessThan60Percent);
            SectorPage.sector(Sector.random());
        });
        Then("^short term permit check your answers page has correct heading label$", () -> {
            CheckYourAnswerPage.untilElementIsPresent("//h1[@class='govuk-heading-xl']", SelectorType.XPATH,10, TimeUnit.SECONDS);
            CheckYourAnswerPage.hasPageHeading();
        });
        Then("^the short term check your answers page has reference number$", () -> {
            BasePermitPage.getReferenceFromPage();
        });
        Then("^Short term application answers are displayed on the check your answers page$", () -> {
            String licence = CheckYourAnswerPage.getAnswer(Licence);
            assertThat(licence, StringContains.containsString(operatorStore.getCurrentLicence().get().getLicenceNumber()));
            String cabotage = CheckYourAnswerPage.getAnswer(Cabotage);
            Assert.assertEquals("I confirm that I will not undertake cabotage journeys using an ECMT permit.", cabotage);
            String certificates = CheckYourAnswerPage.getAnswer(CertificatesRequired);
            Assert.assertEquals("I understand that I must obtain and carry the appropriate Certificate of Compliance and Certificate of Roadworthiness for each vehicle and trailer I intend to use with this permit.", certificates);
            String permitType = CheckYourAnswerPage.getAnswer(PermitType);
            Assert.assertEquals("Short-term ECMT",permitType );
            String actualPermitUsage = CheckYourAnswerPage.getAnswer(PermitsUsage);
            String s = toShortTermPermitUsage(CheckYourAnswersPageSteps.permitUsage.get("permit.usage"));
            Assert.assertEquals(s,actualPermitUsage);
        });
        Then("^I am navigated to the short term declaration page$", org.dvsa.testing.lib.newPages.permits.pages.DeclarationPage::untilOnPage);
        Then("^I am on the short term permits overview page with check your answers section marked as complete$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(OverviewSection.CheckYourAnswers, PermitStatus.COMPLETED);
        });
        Then("^the declaration section gets enabled to be clicked and section status changes to NOT STARTED YET$", OverviewPage::declarationIsNotStartedYet);
        And("^I click Check your answers link on the overview page again$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.CheckYourAnswers);
        });
        Then("^I am navigated to the short term check your answers page$", CheckYourAnswerPage::hasPageHeading);
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