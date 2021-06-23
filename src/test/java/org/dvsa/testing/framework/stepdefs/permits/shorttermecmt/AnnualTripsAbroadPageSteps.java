package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import activesupport.number.Int;
import cucumber.api.java8.En;
import cucumber.api.java8.StepdefBody;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EmissionStandardsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.AnnualTripsAbroadPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

public class AnnualTripsAbroadPageSteps implements En {

    public AnnualTripsAbroadPageSteps(OperatorStore operatorStore, World world) {
        And("^I am on short term ECMT annual trips abroad page$", () -> {
            CommonSteps.clickToPermitTypePage(world);
            ECMTShortTermJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT,operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ECMTShortTermJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore);
            ECMTShortTermJourney.getInstance(). licencePage(operatorStore,world);
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
            NumberOfPermitsPageJourneySteps.completeECMTPage();
            EmissionStandardsPageJourneySteps.completePage();
        });
        And("^the page heading on short term ECMT Annual Trips Abroad page is displayed correctly$", () -> {
            String heading = AnnualTripsAbroadPage.getPageHeading();
            assertEquals("How many international trips did you make in 2019 using this licence?", heading);
        });
        And("^the warning message on short term ECMT Annual Trips Abroad Page is displayed correctly$", () -> {
            String firstGuidanceText = AnnualTripsAbroadPage.getFirstGuidanceText();
            assertEquals("You can use the number of trips done in 2020 if you did not operate during 2019 or if you are a new operator.\n"
                    + "If you have not done any international trips, you are unlikely to be awarded an ECMT permit. We intend to prioritise applications from hauliers who make the most international journeys.\n"
                    + "You will be required to provide evidence of the trips you made.", firstGuidanceText);

            String secondGuidanceText = AnnualTripsAbroadPage.getSecondGuidanceText();
            assertEquals("Help calculating your international trips", secondGuidanceText);
        });
        And("^the reference number on Short term ECMT Annual Trips Abroad Page is displayed correctly$", () -> {
            String expectedLicenceNumber = operatorStore.getCurrentLicenceNumber().orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber = BasePermitPage.getReferenceFromPage();
            Assert.assertThat(actualReferenceNumber, containsString(expectedLicenceNumber));
        });
        When ("^I select help calculating your international trips$", AnnualTripsAbroadPage::helpCalculatingInternationalTrips);
        Then ("^I should see the summary text$", AnnualTripsAbroadPage::isSummaryTextPresent);
        When ("^I select the help calculating international trips again$", AnnualTripsAbroadPage::helpCalculatingInternationalTrips);
        When ("^I should not see the summary text$", () -> {
            Assert.assertFalse(AnnualTripsAbroadPage.isSummaryTextPresent());
        });
        When ("^I select save and continue without entering any value$", BasePermitPage::saveAndContinue);
        When ("^I should see the error message$", () -> {
            String errorText = AnnualTripsAbroadPage.getErrorText();
            assertEquals("Enter the number of trips you carried out over the past 12 months", errorText);
        });
        When ("^I select save and return to overview without entering any value$", BasePermitPage::overview);
        When ("^I specify an invalid ([\\w\\-]+) of annual trips in short term 2020$", (StepdefBody.A1<String>) AnnualTripsAbroadPage::quantity);
        Then  ("^I should get the specific validation message for invalid value$", () -> {
            String errorText = AnnualTripsAbroadPage.getErrorText();
            assertEquals("You must enter a valid whole number", errorText);
        });
        And  ("^I specify more than 6 digit ([\\w\\-]+) of annual trips in short term ECMT$", (StepdefBody.A1<String>) AnnualTripsAbroadPage::quantity);
        Given("^I specify a valid input in short term ECMT annual trips abroad page$", () -> {
            AnnualTripsAbroadPage.quantity(Int.random(0,999999));
        });
        Given("^I select save and return overview link on annual trips abroad page$", AnnualTripsAbroadPage::returnToOverview);
        Then("^the user is navigated to the short term ECMT overview page with the status as completed$", () -> {
            OverviewPageJourneySteps.checkStatus(OverviewSection.AnnualTripsAbroad, PermitStatus.COMPLETED);
        });

  }
}
