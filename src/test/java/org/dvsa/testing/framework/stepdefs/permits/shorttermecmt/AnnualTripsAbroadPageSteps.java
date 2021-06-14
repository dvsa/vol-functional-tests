package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import activesupport.number.Int;
import cucumber.api.java8.En;
import cucumber.api.java8.StepdefBody;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.AnnualTripsAbroadPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.permits.pages.EmissionStandardsPage;
import org.dvsa.testing.lib.newPages.permits.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;
import org.junit.Assert;

import static org.hamcrest.CoreMatchers.containsString;

public class AnnualTripsAbroadPageSteps implements En {

    public AnnualTripsAbroadPageSteps(OperatorStore operatorStore, World world) {
        And("^I am on short term ECMT annual trips abroad page$", () -> {
            CommonSteps.clickToPermitTypePage(world);
            ECMTShortTermJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT,operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ECMTShortTermJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore);
            ECMTShortTermJourney.getInstance(). licencePage(operatorStore,world);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
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
        });
        And("^the page heading on short term ECMT Annual Trips Abroad page is displayed correctly$", AnnualTripsAbroadPage::hasPageHeading);
        And("^the warning message on short term ECMT Annual Trips Abroad Page is displayed correctly$", AnnualTripsAbroadPage::hasWarningText);
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
        When ("^I should see the error message$", AnnualTripsAbroadPage::hasErrorText);
        When ("^I select save and return to overview without entering any value$", BasePermitPage::overview);
        When ("^I specify an invalid ([\\w\\-]+) of annual trips in short term 2020$", (StepdefBody.A1<String>) AnnualTripsAbroadPage::quantity);
        Then  ("^I should get the specific validation message for invalid value$", AnnualTripsAbroadPage::hasNegativeNumberErrorText);
        And  ("^I specify more than 6 digit ([\\w\\-]+) of annual trips in short term ECMT$", (StepdefBody.A1<String>) AnnualTripsAbroadPage::quantity);
        Then  ("^I should get the specific validation message for invalid input$", AnnualTripsAbroadPage::hasNegativeNumberErrorText);
        Given("^I specify a valid input in short term ECMT annual trips abroad page$", () -> {
            AnnualTripsAbroadPage.quantity(Int.random(0,999999));
        });
        Given("^I select save and return overview link on annual trips abroad page$", AnnualTripsAbroadPage::returnToOverview);
        Then("^the user is navigated to the short term ECMT overview page with the status as completed$", () -> {
            OverviewPage.checkStatus(OverviewSection.AnnualTripsAbroad, PermitStatus.COMPLETED);
        });

  }
}
