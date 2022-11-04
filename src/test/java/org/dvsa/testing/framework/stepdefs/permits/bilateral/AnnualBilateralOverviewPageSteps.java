package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.EssentialInformationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.PeriodSelectionPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.Country;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.*;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnnualBilateralOverviewPageSteps extends BasePage implements En {
    public AnnualBilateralOverviewPageSteps(World world) {
        Then("^the status of Morocco under answers questions for individual countries section is marked as Completed$", () -> {
            OverviewPageJourney.checkBilateralStatus(OverviewSection.Countries, PermitStatus.COMPLETED);
        });
        Then("^the status of answers questions for individual countries as complete$", () -> {
            String s1 = BasePage.getElementValueByText("//li[2]//ul[1]//li[1]//span[2]", SelectorType.XPATH);
            assertEquals(s1, "COMPLETED");
        });
        And("^I click on Turkey country link on the Application overview page$", () -> {
            OverviewPage.clickCountrySection(Country.Turkey);
        });
        And("^I click on Ukraine country link on the Application overview page$", () -> {
            OverviewPage.clickCountrySection(Country.Ukraine);
        });
        When("^I submit the application on selection of Morocco link on overview page$", () -> {
            completeMoroccoBilateralJourneyUntilDeclarationPage(PeriodType.MoroccoStandardMultipleJourney);
        });
        When("^I submit the application for standard single journey on selection of Morocco link on overview page$", () -> {
            completeMoroccoBilateralJourneyUntilDeclarationPage(PeriodType.MoroccoStandardSingleJourney);
        });
        When("^I submit the application for empty entry single journey on selection of Morocco link on overview page$", () -> {
            completeMoroccoBilateralJourneyUntilDeclarationPage(PeriodType.MoroccoEmptyEntry);
        });
        When("^I submit the application for Hors Contingent single journey on selection of Morocco link on overview page$", () -> {
            completeMoroccoBilateralJourneyUntilDeclarationPage(PeriodType.MoroccoHorsContingency);
        });
    }

    public void completeMoroccoBilateralJourneyUntilDeclarationPage(PeriodType typeOfMoroccoJourney) {
        OverviewPage.clickCountrySection(Country.Morocco);
        EssentialInformationPage.untilOnPage();
        assertEquals(BasePermitPage.getCountry(), AnnualBilateralJourney.getCountry());
        EssentialInformationPageJourney.completePage();
        PeriodSelectionPageJourney.hasMoroccoPageHeading();
        assertEquals(BasePermitPage.getCountry(), AnnualBilateralJourney.getCountry());
        AnnualBilateralJourney.completePeriodTypePage(typeOfMoroccoJourney);
        NumberOfPermitsPage.untilOnPage();
        assertEquals(getElementValueByText("//div[contains(text(),'Morocco')]",SelectorType.XPATH), AnnualBilateralJourney.getCountry());
        NumberOfPermitsPageJourney.hasPageHeading();
        BasePermitPage.saveAndContinue();
        NumberOfPermitsPageJourney.hasBilateralErrorMessage();
        NumberOfPermitsPageJourney.completePage();
        CheckYourAnswerPage.untilOnPage();
        CheckYourAnswerPage.clickConfirmAndReturnToOverview();
    }
}
