package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.CancellationPage;
import org.dvsa.testing.lib.newPages.permits.pages.EssentialInformationPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PeriodSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PermitUsagePage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class CabotagePageSteps extends BasePermitPage implements En {

    private int numberOfCountries;

    public CabotagePageSteps(OperatorStore operatorStore, World world) {
        And ("^I am on the Bilateral Cabotage Page with more than one countries selected$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            numberOfCountries = size("(//input[@type='checkbox'])", SelectorType.XPATH);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            annualBilateralOverviewPageUntilPeriodSelectionPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());

        });
        Then("^the page heading on the bilateral cabotage page is displayed correctly$", () -> {
            Assert.assertEquals(getText("//h1[@class='govuk-fieldset__heading']", SelectorType.XPATH),"Do you need to carry out cabotage?");
        });
        And ("^the advisory texts on the bilateral cabotage page are displayed correctly$", BilateralJourneySteps::assertBilateralCabotageAdvisoryTexts);
        When("^select save and continue without confirming$", BasePermitPage::saveAndContinue);
        Then("^the cabotage relevant error message is displayed$", () -> {
            Assert.assertEquals(getText("//p[@class='error__text']", SelectorType.XPATH),"Please select one option");
        });
        When("^I select 'no' button$", BilateralJourneySteps::clickNoToCabotage);
        Then("^the relevant advisory text message is displayed$", BilateralJourneySteps::assertNonCabotageConfirmationAdvisoryTexts);
        When("^I select 'yes' button and save and continue$", BilateralJourneySteps::clickYesToCabotage);
        And ("^I am on the Bilateral Cabotage Page with norway selection", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            annualBilateralOverviewPageUntilPeriodSelectionPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());
        });
        Then("^I m navigated to the correct page depending upon whether there is just one country selected or more than one", () -> {
            if(this.numberOfCountries > 1) {
                org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
                isElementNotPresent("//a[contains(text(),'Norway')]",SelectorType.XPATH);
            }
            else {
                CancellationPage.untilOnPage();
                CancellationPage.assertAdvisoryTextOnCancelApplicationPage();
            }
        });
        And ("^the advisory texts on the bilateral standard and cabotage permits page are displayed correctly", BilateralJourneySteps::assertStandardAndCabotagePermitsAdvisoryTexts);
        And ("^I am on the cabotage page for standard and cabotage permits with more than one countries selected", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            annualBilateralOverviewPageUntilPeriodSelectionPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());
        });
        And ("^I am on the Bilateral Cabotage Page for standard and cabotage permits with norway selection", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            annualBilateralOverviewPageUntilPeriodSelectionPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());
        });
        And ("^I select standard permits no cabotage application", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
            annualBilateralOverviewPageUntilPeriodSelectionPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.journeyType(JourneyType.random());
        });
        Then("^the cabotage page is not displayed in the application flow and taken to number of permits page", () -> {
            isPath("//permits/application/\\d+/ipa/\\d+/bi-number-of-permits");
        });
    }

    private void annualBilateralOverviewPageUntilPeriodSelectionPage() {
        org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
        org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCountrySection(Country.Norway);
        EssentialInformationPage.untilOnPage();
        saveAndContinue();
        PeriodSelectionPage.untilOnPage();
    } // Could look a another method where it does the journey with differences in if statements
}
