package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import activesupport.system.Properties;
import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.Driver.DriverUtils;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.*;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.isPath;

public class AnnualBilateralDeclarationPageSteps extends DriverUtils implements En {
    public AnnualBilateralDeclarationPageSteps(OperatorStore operatorStore, World world, LicenceStore licenceStore) {
        And("^I'm on the annual bilateral cabotage only declaration page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BilateralJourneySteps.clickYesToCabotage();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.numberOfPermits();
            BasePermitPage.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.selectDeclaration();
        });
        Then("^the application reference number and advisory text are displayed correctly$", () -> {
            String actualReference = BasePermitPage.getReference();
            licenceStore.setReferenceNumber(actualReference);
            Assert.assertTrue(String.valueOf(actualReference.contains(operatorStore.getCurrentLicenceNumber().toString().substring(9,17))),true);
            DeclarationPage.pageHeading();
            DeclarationPage.advisoryTexts();
        });
        Then("^I select the declaration link on the overview page$", () -> {
           OverviewPage.selectDeclaration();
        });
        Then("^I am on the Annual Bilateral application submitted page$", () -> {
            isPath("/permits/application/\\d+/submitted/");
        });
        Then("^I am continuing on the on-going Annual Bilateral application$", () -> {
            deleteCookies();
            refreshPage();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "auth/login/").toString());

            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            String licence1= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
        });
        Then("^I'm on the annual bilateral StandardPermitsNoCabotage only declaration page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.numberOfPermits();
            BasePermitPage.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.selectDeclaration();
        });
        Then("^I'm on the annual bilateral StandardAndCabotagePermits only declaration page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardAndCabotagePermits,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BilateralJourneySteps.clickYesToCabotage();
            AnnualBilateralJourney.getInstance().cabotageConfirmation(world,licenceStore);
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.numberOfPermits();
            BasePermitPage.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.selectDeclaration();
        });
        Then("^I continue with the on-going Annual Bilateral application$", () -> {
            BasePermitPage.back();
            HomePage.selectTab(Tab.PERMITS);
            String licence1= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
        });
        Then("^I am taken to the bilateral declaration Page$", () -> {
              DeclarationPage.pageHeading();
        });
        Then("^there's a guidance notes link to the correct gov page$", () -> {
            Assert.assertTrue("Unable to find guidance notes link", DeclarationPage.hasGuidanceNotes());
        });
        When("^I submit my annual bilateral declaration$", () -> {
            AnnualBilateralJourney.getInstance().declare(true);
        });
    }
}
