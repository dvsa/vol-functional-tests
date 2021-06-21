package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import activesupport.number.Int;
import io.cucumber.java8.En;
import io.cucumber.java8.StepDefinitionBody;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitUsage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.external.permit.shorttermecmt.PeriodSelectionPageOne.*;
import static org.hamcrest.CoreMatchers.containsString;

public class AnnualTripsAbroadPageSteps implements En {

    public AnnualTripsAbroadPageSteps(OperatorStore operatorStore, World world) {
        And("^I am on short term ECMT annual trips abroad page$", () -> {
            CommonSteps.clickToPermitTypePage(world);
            ECMTShortTermJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.ShortTermECMT,operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ECMTShortTermJourney.getInstance().shortTermType(ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore);
            ECMTShortTermJourney.getInstance(). licencePage(operatorStore,world);
            OverviewPage.select(OverviewPage.Section.HowwillyouusethePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            CabotagePage.cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.CertificatesRequiredConfirmation();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            NumberOfPermitsPage.euro5OrEuro6permitsValue();
            BasePermitPage.saveAndContinue();
            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();
        });
        And("^the page heading on short term ECMT Annual Trips Abroad page is displayed correctly$", AnnualTripsAbroadPage::pageHeading);
        And("^the warning message on short term ECMT Annual Trips Abroad Page is displayed correctly$", AnnualTripsAbroadPage::warningText);
        And("^the reference number on Short term ECMT Annual Trips Abroad Page is displayed correctly$", () -> {
            String expectedLicenceNumber = operatorStore.getCurrentLicenceNumber().orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber = AnnualTripsAbroadPage.reference();
            Assert.assertThat(actualReferenceNumber, containsString(expectedLicenceNumber));
        });
        When ("^I select help calculating your international trips$", AnnualTripsAbroadPage::helpCalculatingInternationalTrips);
        Then ("^I should see the summary text$", AnnualTripsAbroadPage::summaryText);
        When ("^I select the help calculating international trips again$", AnnualTripsAbroadPage::helpCalculatingInternationalTrips);
        When ("^I should not see the summary text$", () -> {
            Assert.assertFalse(AnnualTripsAbroadPage.summaryText());
        });
        When ("^I select save and continue without entering any value$", BasePermitPage::saveAndContinue);
        When ("^I should see the error message$", AnnualTripsAbroadPage::errorText);
        When ("^I select save and return to overview without entering any value$", BasePermitPage::overview);
        When ("^I specify an invalid ([\\w\\-]+) of annual trips in short term 2020$", (StepDefinitionBody.A1<String>) AnnualTripsAbroadPage::quantity);
        Then  ("^I should get the specific validation message for invalid value$", AnnualTripsAbroadPage::negativeErrorText);
        And  ("^I specify more than 6 digit ([\\w\\-]+) of annual trips in short term ECMT$", (StepDefinitionBody.A1<String>) AnnualTripsAbroadPage::quantity);
        Then  ("^I should get the specific validation message for invalid input$", AnnualTripsAbroadPage::inValidErrorText);
        Given("^I specify a valid input in short term ECMT annual trips abroad page$", () -> {
            AnnualTripsAbroadPage.quantity(Int.random(0,999999));
        });
        Given("^I select save and return overview link on annual trips abroad page$", AnnualTripsAbroadPage::intensity);
        Then("^the user is navigated to the short term ECMT overview page with the status as completed$", () -> {
         //   boolean annualTripsAbroad =  BaseOverviewPage.checkStatus(PermitSection.AnnualTripsAbroad.toString(), PermitStatus.COMPLETED);
          //            //  Assert.assertTrue(annualTripsAbroad);
        });

  }
}
