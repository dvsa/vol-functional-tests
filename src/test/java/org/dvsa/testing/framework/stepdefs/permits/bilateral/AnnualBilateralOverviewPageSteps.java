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
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.framework.pageObjects.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class AnnualBilateralOverviewPageSteps extends BasePage implements En {
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
        Then("^the status of Morocco under answers questions for individual countries section is marked as Completed$", () -> {
            OverviewPageJourney.checkBilateralStatus(OverviewSection.Countries, PermitStatus.COMPLETED);
        });
        Then("^the status of answers questions for individual countries as complete$", () -> {
            String s1 = BasePage.getElementValueByText("//li[2]//ul[1]//li[1]//span[2]", SelectorType.XPATH);
            Assert.assertEquals(s1, "COMPLETED");
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
        Assert.assertEquals(BasePermitPage.getCountry(), operatorStore.getCountry());
        EssentialInformationPageJourney.completePage();
        PeriodSelectionPageJourney.hasMoroccoPageHeading();
        Assert.assertEquals(BasePermitPage.getCountry(), operatorStore.getCountry());
        AnnualBilateralJourney.getInstance().bilateralPeriodType(typeOfMoroccoJourney, operatorStore);
        PeriodSelectionPage.saveAndContinue();
        NumberOfPermitsPage.untilOnPage();
        Assert.assertEquals(getElementValueByText("//div[contains(text(),'Morocco')]",SelectorType.XPATH), operatorStore.getCountry());
        NumberOfPermitsPageJourney.hasPageHeading();
        BasePermitPage.saveAndContinue();
        NumberOfPermitsPageJourney.hasBilateralErrorMessage();
        NumberOfPermitsPageJourney.completePage();
        CheckYourAnswerPage.untilOnPage();
        CheckYourAnswerPage.clickConfirmAndReturnToOverview();
    }
}
