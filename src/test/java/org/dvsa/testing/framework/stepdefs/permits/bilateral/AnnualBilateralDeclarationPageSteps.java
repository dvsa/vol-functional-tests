package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import activesupport.system.Properties;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.Driver.DriverUtils;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.newPages.external.pages.bilateralsOnly.BilateralJourneySteps;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertTrue;

public class AnnualBilateralDeclarationPageSteps extends BasePage implements En {
    public AnnualBilateralDeclarationPageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {
        And("^I'm on the annual bilateral cabotage only declaration page$", () -> {
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
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BilateralJourneySteps.clickYesToCabotage();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPageJourney.completePage();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPageJourney.clickOverviewSection(OverviewSection.BilateralDeclaration);
        });
        Then("^the application reference number and advisory text are displayed correctly$", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            licenceStore.setReferenceNumber(actualReference);
            Assert.assertTrue(String.valueOf(actualReference.contains(operatorStore.getCurrentLicenceNumber().toString().substring(9,17))),true);
            DeclarationPageJourney.hasPageHeading();
            assertTrue(DeclarationPage.isBilateralAdvisoryTextPresent());
        });
        Then("^I select the declaration link on the overview page$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.BilateralDeclaration);
        });
        Then("^I am on the Annual Bilateral application submitted page$", () -> {
            isPath("/permits/application/\\d+/submitted/");
        });
        Then("^I am continuing on the on-going Annual Bilateral application$", () -> {
            deleteCookies();
            refreshPage();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "auth/login/").toString());

            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            HomePage.PermitsTab.selectFirstOngoingApplication();
        });
        Then("^I'm on the annual bilateral StandardPermitsNoCabotage only declaration page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPageJourney.completePage();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPageJourney.clickOverviewSection(OverviewSection.BilateralDeclaration);
        });
        Then("^I'm on the annual bilateral StandardAndCabotagePermits only declaration page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BilateralJourneySteps.clickYesToCabotage();
            AnnualBilateralJourney.getInstance().cabotageConfirmation(world,licenceStore);
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPageJourney.completePage();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPageJourney.clickOverviewSection(OverviewSection.BilateralDeclaration);
        });
        Then("^I continue with the on-going Annual Bilateral application$", () -> {
            BasePermitPage.back();
            HomePageJourney.selectPermitTab();
            HomePage.PermitsTab.selectFirstOngoingApplication();
        });
        Then("^there's a guidance notes link to the correct gov page$", BasePermitPage::hasInternationalAuthorisationGovGuidanceLink);
        When("^I submit my annual bilateral declaration$", DeclarationPageJourney::completeDeclaration);
    }
}
