package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import activesupport.IllegalBrowserException;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.external.enums.JourneyType;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.junit.Assert;

import java.net.MalformedURLException;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CheckYourAnswersPageSteps extends BasePage implements En {
    public CheckYourAnswersPageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore)throws MalformedURLException, IllegalBrowserException {
        Then("^I am on the Bilateral check your answers page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            BilateralJourneySteps.clickYesToCabotage();
            BilateralJourneySteps.saveAndContinue();
            NumberOfPermitsPageJourney.completePage();
        });

        Then("^Country name displayed on the Bilateral check your answers page is the one clicked on the overview page$", () -> {
            CheckYourAnswerPage.untilOnPage();
            String country = getText("//div[@class='govuk-caption-xl']", SelectorType.XPATH);
            Assert.assertEquals(country, operatorStore.getCountry());
        });

        Then("^the page heading on the check your answers page is correct$", () -> {
            CheckYourAnswerPage.untilOnPage();
            String heading = CheckYourAnswerPage.getPageHeading();
            assertEquals("Check your answers", heading);
        });

        Then("^I see four sections displayed on the table correctly$", () -> {
            assertTrue(BilateralJourneySteps.areSectionsPresent(true));
        });

        Then("^Period type displayed on the check your answers page is the one I selected on the Period selection page$", () -> {
            Assert.assertEquals(BilateralJourneySteps.getPeriodText(), operatorStore.getCurrentBilateralPeriodType().toString());
        });

        Then("^Journey type displayed on the check your answers page is the one I selected on the Permits usage$", () -> {
            Assert.assertEquals(BilateralJourneySteps.getJourney(),PermitUsagePage.getJourney());
        });

        Then("^Value of do you need to carry out cabotage, will always be 'YES'$", () -> {
            assertEquals("Yes", BilateralJourneySteps.getCabotageValue());
        });

        Then("^Value of How many permits you need, will be the one saved on the number of permits page$", () -> {
            String permitlabel = NumberOfPermitsPageJourney.getLabel();
            String permitvalue = String.valueOf(NumberOfPermitsPageJourney.getPermitValue());
            Assert.assertEquals(BilateralJourneySteps.getPermitValue(),permitvalue+" "+permitlabel+"s");
        });

        When("^I click Confirm and return to overview$", CheckYourAnswerPage::clickConfirmAndReturnToOverview);

        Then("^the status of Answer questions for individual countries section for the selected country is set as complete$", () -> {
            String s1 = BasePage.getElementValueByText("//li[2]//ul[1]//li[1]//span[2]",SelectorType.XPATH);
            Assert.assertEquals(s1,"COMPLETED");
        });

        Then("^I click change against Period for which you need permits section$", () -> {
            if (isElementPresent("//div[1]/dd[2]/a[1]", SelectorType.XPATH)){
                scrollAndClick("//div[1]/dd[2]/a[1]", SelectorType.XPATH);
            }
        });

        Then("^I am navigated to the Bilateral period selection page$", PeriodSelectionPage::untilOnPage);

        And("^I change period to be Bilateral and Standard permits on the period selection and continue to be on the check your answers page$", () -> {
          AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardAndCabotagePermits, operatorStore);
          assertTrue(PeriodSelectionPage.isWarningTextPresent());
          PeriodSelectionPage.saveAndContinue();
          PermitUsagePage.untilOnPage();
          PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
          PermitUsagePage.saveAndContinue();
          BilateralJourneySteps.clickNoToCabotage();
          BilateralJourneySteps.saveAndContinue();
          NumberOfPermitsPageJourney.completePage();
          CheckYourAnswerPage.untilOnPage();
        });

        And("^I change period to be Bilateral Standard permits no Cabotage on the period selection and continue to be on the check your answers page$", () -> {
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardPermitsNoCabotage, operatorStore);
            assertTrue(PeriodSelectionPage.isWarningTextPresent());
            PeriodSelectionPage.saveAndContinue();
            PermitUsagePage.untilOnPage();
            PermitUsagePage.journeyType(JourneyType.MultipleJourneys);
            PermitUsagePage.saveAndContinue();
            NumberOfPermitsPageJourney.completePage();
            CheckYourAnswerPage.untilOnPage();
        });

        Then("^Value of Do you need to carry out cabotage, will be as per the selection after changing the period selection to Bilaterals Standard and Cabotage permits$", () -> {
            assertEquals("No", BilateralJourneySteps.getCabotageValue());
        });

        Then("^Do you need to carry out cabotage, will not be displayed if the period type is Bilaterals Standard permits no Cabotage$", () -> {
            assertTrue(isElementNotPresent("//dt[contains(text(),'Do you need to carry out cabotage?')]", SelectorType.XPATH));
        });

        Then("^Value of How many permits you need, will be the one saved on the number of permits page for Bileterals Standard permits no Cabotage$", () -> {
            String permitLabel = NumberOfPermitsPageJourney.getLabel();
            String permitValue = String.valueOf(NumberOfPermitsPageJourney.getPermitValue());
            Assert.assertEquals(BilateralJourneySteps.getCabotageValue(),permitValue + " " + permitLabel + "s");
        });
    }
}
