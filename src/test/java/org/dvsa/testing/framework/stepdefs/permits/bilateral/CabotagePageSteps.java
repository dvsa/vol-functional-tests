package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.*;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.isPath;
import static org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage.*;

public class CabotagePageSteps implements En {
    public CabotagePageSteps(OperatorStore operatorStore, World world, CountrySelectionPage countrySelectionPage) {
        And ("^I am on the Bilateral Cabotage Page with more than one countries selected$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            CountrySelectionPage.countrycount();
            countrySelectionPage.setCountOfCountries(CountrySelectionPage.countrycount());
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            bilateralEssentialInfoContinueButton();
            untilOnPeriodSelectionPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());

        });
        Then("^the page heading on the bilateral cabotage page is displayed correctly$", CabotagePage::pageHeading);
        And ("^the advisory texts on the bilateral cabotage page are displayed correctly$", CabotagePage::advisoryTexts);
        When("^select save and continue without confirming$", BasePermitPage::saveAndContinue);
        Then("^the cabotage relevant error message is displayed$", CabotagePage::errorText);
        When("^I select 'no' button$", CabotagePage::noButton);
        Then("^the relevant advisory text message is displayed$", CabotagePage::advisoryTextforNonCabotageConfirmation);
        When("^I select 'yes' button and save and continue$", CabotagePage::yesButton);
        And ("^I am on the Bilateral Cabotage Page with norway selection", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            bilateralEssentialInfoContinueButton();
            untilOnPeriodSelectionPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());
        });
        Then("^I m navigated to the correct page depending upon whether there is just one country selected or more than one", () -> {
            if(countrySelectionPage.getCountOfCountries()>1)
            {
                OverviewPage.untilOnOverviewPage();
                OverviewPage.norwaySectionNotExists();
            }
            else
            {
                CancelApplicationPage.untilOnPage();
                CancelApplicationPage.hasAdvisoryText();
            }
        });
        And ("^the advisory texts on the bilateral standard and cabotage permits page are displayed correctly", CabotagePage::advisoryTextsStandardAndCabotagePermits);
        And ("^I am on the cabotage page for standard and cabotage permits with more than one countries selected", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            bilateralEssentialInfoContinueButton();
            untilOnPeriodSelectionPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());
        });
        And ("^I am on the Bilateral Cabotage Page for standard and cabotage permits with norway selection", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            bilateralEssentialInfoContinueButton();
            untilOnPeriodSelectionPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());
        });
        And ("^I select standard permits no cabotage application", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            bilateralEssentialInfoContinueButton();
            untilOnPeriodSelectionPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());
        });
        Then("^the cabotage page is not displayed in the application flow and taken to number of permits page", () -> {
            isPath("//permits/application/\\d+/ipa/\\d+/bi-number-of-permits");
        });
    }
}