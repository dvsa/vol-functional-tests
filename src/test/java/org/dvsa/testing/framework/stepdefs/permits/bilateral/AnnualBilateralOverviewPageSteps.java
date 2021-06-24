package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EssentialInformationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.PeriodSelectionPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.getElementValueByText;
import static org.dvsa.testing.lib.pages.BasePage.isPath;
import static org.dvsa.testing.lib.pages.external.permit.BasePermitPage.getCountry;

public class AnnualBilateralOverviewPageSteps implements En {
    public AnnualBilateralOverviewPageSteps(LicenceStore licenceStore, OperatorStore operatorStore, World world) {
        Then("^the status for the declaration section is complete$", () -> {
            String s2 = BasePage.getElementValueByText("//li[3]//ul[1]//li[1]//span[@class='overview__status green']", SelectorType.XPATH);
            Assert.assertEquals(s2, "COMPLETED");
        });
        And("^I'm on bilateral overview page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
        });
        And("^I'm on bilateral overview page with multiple countries selected$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().allCountries(operatorStore);
        });
        And("^I have completed till check your answers page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralCabotagePermitsOnly, operatorStore);
            PermitUsagePage.untilOnPage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BilateralJourneySteps.clickYesToCabotage();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPageJourney.completePage();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
        });
        Then("^the status of Morocco under answers questions for individual countries section is marked as Completed$", () -> {
            OverviewPageJourney.checkStatus(OverviewSection.Countries, PermitStatus.COMPLETED);
        });
        Then("^I select submit and pay link$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.SubmitAndPay);
        });
        Then("^I am taken to the overview page with the status as completed$", () -> {
            String s1 = BasePage.getElementValueByText("//li[3]//ul[1]//li[1]//span[2]", SelectorType.XPATH);
            Assert.assertEquals(s1, "COMPLETED");
        });
        Then("^the status of answers questions for individual countries as complete$", () -> {
            String s1 = BasePage.getElementValueByText("//li[2]//ul[1]//li[1]//span[2]", SelectorType.XPATH);
            Assert.assertEquals(s1, "COMPLETED");
        });
        Then("^I select the edit countries button$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.EditCountrySelection);
        });
        Then("^I declare and save and return to overview$", () -> {
            DeclarationPage.confirmDeclaration();
            BasePermitPage.overview();
        });
        Then("^I'm navigated to countries selection page$", () -> {
            isPath("/permits/application/\\d+/countries/\\d+");
        });
        When("^the hyperlink for select countries is selected$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.Countries);
        });
        Then("^I am navigated to the relevant page$", () -> {
            isPath("//permits/application/\\d+/countries/");
        });
        And("^I click on Turkey country link on the Application overview page$", () -> {
            OverviewPage.clickCountrySection(Country.Turkey);
        });
        And("^I click on Ukraine country link on the Application overview page$", () -> {
            OverviewPage.clickCountrySection(Country.Ukraine);
        });
        When("^I submit the application on selection of Morocco link on overview page$", () -> {
            completeMoroccoBilateralJourneyUntilDeclarationPage(PeriodType.MoroccoStandardMultipleJourney, operatorStore);
        });
        When("^I submit the application for standard single journey on selection of Morocco link on overview page$", () -> {
            completeMoroccoBilateralJourneyUntilDeclarationPage(PeriodType.MoroccoStandardSingleJourney, operatorStore);
        });
        When("^I submit the application for empty entry single journey on selection of Morocco link on overview page$", () -> {
            completeMoroccoBilateralJourneyUntilDeclarationPage(PeriodType.MoroccoEmptyEntry, operatorStore);
        });
        When("^I submit the application for Hors Contingent single journey on selection of Morocco link on overview page$", () -> {
            completeMoroccoBilateralJourneyUntilDeclarationPage(PeriodType.MoroccoHorsContingency, operatorStore);
        });
    }

    public void completeMoroccoBilateralJourneyUntilDeclarationPage(PeriodType typeOfMoroccoJourney, OperatorStore operatorStore) {
        OverviewPage.clickCountrySection(Country.Morocco);
        EssentialInformationPage.untilOnPage();
        Assert.assertEquals(getCountry(), operatorStore.getCountry());
        EssentialInformationPageJourney.completePage();
        PeriodSelectionPageJourney.hasMoroccoPageHeading();
        Assert.assertEquals(getCountry(), operatorStore.getCountry());
        AnnualBilateralJourney.getInstance().bilateralPeriodType(typeOfMoroccoJourney, operatorStore);
        PeriodSelectionPage.saveAndContinue();
        NumberOfPermitsPage.untilOnPage();
        Assert.assertEquals(getElementValueByText("//div[contains(text(),'Morocco')]",SelectorType.XPATH), operatorStore.getCountry());
        NumberOfPermitsPageJourney.hasPageHeading();
        BasePermitPage.saveAndContinue();
        NumberOfPermitsPageJourney.hasBilateralErrorMessage();
        NumberOfPermitsPageJourney.completePage();
        CheckYourAnswerPage.untilOnPage();
        CheckYourAnswerPage.saveAndContinue();
    }
}
