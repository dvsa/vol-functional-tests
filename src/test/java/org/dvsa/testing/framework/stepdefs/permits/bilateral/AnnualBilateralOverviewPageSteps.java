package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EssentialInformationPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.*;
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
            String s2= BasePage.getElementValueByText("//li[3]//ul[1]//li[1]//span[@class='overview__status green']",SelectorType.XPATH);
            Assert.assertEquals(s2,"COMPLETED");
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
            System.out.println(operatorStore.getCountry());
        });
        And("^I have completed till check your answers page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BilateralJourneySteps.clickYesToCabotage();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
            BasePermitPage.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
        });
        Then("^the status of Morocco under answers questions for individual countries section is marked as Completed$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(OverviewSection.Countries, PermitStatus.COMPLETED);
        });
        Then("^I select submit and pay link$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.SubmitAndPay);
        });
        Then("^I am taken to the overview page with the status as completed$", () -> {
            String s1= BasePage.getElementValueByText("//li[3]//ul[1]//li[1]//span[2]",SelectorType.XPATH);
            Assert.assertEquals(s1,"COMPLETED");
        });
        Then("^the status of answers questions for individual countries as complete$", () -> {
         String s1= BasePage.getElementValueByText("//li[2]//ul[1]//li[1]//span[2]",SelectorType.XPATH);
         Assert.assertEquals(s1,"COMPLETED");
        });
        Then("^I select the edit countries button$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.EditCountrySelection);
        });
        Then("^I declare and save and return to overview$", () -> {
            DeclarationPage.confirmDeclaration();
            BasePermitPage.overview();
        });
        Then("^I'm navigated to countries selection page$", () -> {
         isPath("/permits/application/\\d+/countries/\\d+");
        });
        When("^the hyperlink for select countries is selected$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.Countries);
        });
        Then("^I am navigated to the relevant page$", () -> {
            isPath("//permits/application/\\d+/countries/");
        });
        Then("^I m navigated to bilateral overview page$", org.dvsa.testing.lib.newPages.permits.pages.OverviewPage::hasPageHeading);

        And("^I click on Turkey country link on the Application overview page$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCountrySection(Country.Turkey);
        });
        And("^I click on Ukraine country link on the Application overview page$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCountrySection(Country.Ukraine);
        });
        When("^I submit the application on selection of Morocco link on overview page$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCountrySection(Country.Morocco);
            EssentialInformationPage.untilOnPage();
            Assert.assertEquals(getCountry(), operatorStore.getCountry());
            EssentialInformationPageJourneySteps.completePage();
            PeriodSelectionPage.hasMoroccoPageHeading();
            Assert.assertEquals(getCountry(), operatorStore.getCountry());
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.MoroccoStandardMultipleJourney,operatorStore);
            PeriodSelectionPage.saveAndContinue();
            NumberOfPermitsPage.untilOnPage();
            Assert.assertEquals(getElementValueByText("//div[contains(text(),'Morocco')]",SelectorType.XPATH),operatorStore.getCountry());
            NumberOfPermitsPage.hasPageHeading();
            Assert.assertTrue("Standard multiple journey permit",true);
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.hasBilateralErrorMessage();
            NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
            BasePermitPage.saveAndContinue();
            CheckYourAnswerPage.untilOnPage();
            CheckYourAnswerPage.saveAndContinue();
        });
        When("^I submit the application for standard single journey on selection of Morocco link on overview page$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCountrySection(Country.Morocco);
            EssentialInformationPage.untilOnPage();
            Assert.assertEquals(getCountry(), operatorStore.getCountry());
            EssentialInformationPageJourneySteps.completePage();
            PeriodSelectionPage.hasMoroccoPageHeading();
            Assert.assertEquals(getCountry(), operatorStore.getCountry());
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.MoroccoStandardSingleJourney,operatorStore);
            PeriodSelectionPage.saveAndContinue();
            NumberOfPermitsPage.untilOnPage();
            Assert.assertEquals(getElementValueByText("//div[contains(text(),'Morocco')]",SelectorType.XPATH),operatorStore.getCountry());
            NumberOfPermitsPage.hasPageHeading();
            Assert.assertTrue("Standard single journey permit", true);
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.hasBilateralErrorMessage();
            NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
            BasePermitPage.saveAndContinue();
            CheckYourAnswerPage.untilOnPage();
            CheckYourAnswerPage.saveAndContinue();
        });
        When("^I submit the application for empty entry single journey on selection of Morocco link on overview page$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCountrySection(Country.Morocco);
            EssentialInformationPage.untilOnPage();
            Assert.assertEquals(getCountry(), operatorStore.getCountry());
            EssentialInformationPageJourneySteps.completePage();
            PeriodSelectionPage.hasMoroccoPageHeading();
            Assert.assertEquals(getCountry(), operatorStore.getCountry());
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.MoroccoEmptyEntry,operatorStore);
            PeriodSelectionPage.saveAndContinue();
            NumberOfPermitsPage.untilOnPage();
            Assert.assertEquals(getElementValueByText("//div[contains(text(),'Morocco')]",SelectorType.XPATH), operatorStore.getCountry());
            NumberOfPermitsPage.hasPageHeading();
            Assert.assertTrue("Empty Entry single journey permit", true);
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.hasBilateralErrorMessage();
            NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
            BasePermitPage.saveAndContinue();
            CheckYourAnswerPage.untilOnPage();
            CheckYourAnswerPage.saveAndContinue();
        });
        When("^I submit the application for Hors Contingent single journey on selection of Morocco link on overview page$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCountrySection(Country.Morocco);
            EssentialInformationPage.untilOnPage();
            Assert.assertEquals(getCountry(), operatorStore.getCountry());
            EssentialInformationPageJourneySteps.completePage();
            PeriodSelectionPage.hasMoroccoPageHeading();
            Assert.assertEquals(getCountry(), operatorStore.getCountry());
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.MoroccoHorsContingency, operatorStore);
            PeriodSelectionPage.saveAndContinue();
            NumberOfPermitsPage.untilOnPage();
            Assert.assertEquals(getElementValueByText("//div[contains(text(),'Morocco')]",SelectorType.XPATH),operatorStore.getCountry());
            NumberOfPermitsPage.hasPageHeading();
            Assert.assertTrue("Empty Entry single journey permit",true);
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.hasBilateralErrorMessage();
            NumberOfPermitsPage.setNumberOfPermitsAndSetRespectiveValues();
            BasePermitPage.saveAndContinue();
            CheckYourAnswerPage.untilOnPage();
            CheckYourAnswerPage.saveAndContinue();
        });
    } //TODO: More refactoring to do, there's so much copy and pasting. UGGHHHHH...
}
