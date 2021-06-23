package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.BaseJourney;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.getElementValueByText;

public class AnnualBilateralSubmittedPageSteps implements En {
    public AnnualBilateralSubmittedPageSteps(LicenceStore licenceStore, OperatorStore operatorStore, World world) {
        And("^I'm on the annual bilateral cabotage only submitted  page$", () -> {
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
            NumberOfPermitsPageJourneySteps.completePage();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.BilateralDeclaration);
            DeclarationPageJourneySteps.completeDeclaration();
            AnnualBilateralJourney.getInstance()
                    .permitFee();

            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            SubmittedPage.untilOnPage();
        });
        Then("^my application reference should be displayed$", () -> {
            String actualReference = getElementValueByText("//div[@class='govuk-panel__body'] ", SelectorType.XPATH);
            Assert.assertEquals(actualReference.contains(operatorStore.getCurrentLicenceNumber().toString().substring(9,18)),true);
        });
        Then("^I should see the view receipt link$", () -> {
            Assert.assertTrue("'View  Receipt' link  should be displayed but wasn't", SubmittedPage.hasViewReceipt());
        });
        And("^I'm on the annual bilateral submitted page for my active application$", () -> {
            BaseJourney.getInstance().go(ApplicationType.EXTERNAL);
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            String licence1 = operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.BilateralDeclaration);
            DeclarationPageJourneySteps.completeDeclaration();
        });
        Then("^I should not see the view receipt link$", () -> {
            Assert.assertFalse("'View  Receipt' link  should NOT be displayed but was", SubmittedPage.hasViewReceipt()
            );
        });
        When("^I select finish$", SubmittedPage::goToPermitsDashboard);
        And ("^I'm on the annual bilateral StandardAndCabotagePermits only submitted page$", () -> {
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
            NumberOfPermitsPageJourneySteps.completePage();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.BilateralDeclaration);
            DeclarationPageJourneySteps.completeDeclaration();
            AnnualBilateralJourney.getInstance()
                    .permitFee();

            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            SubmittedPage.untilOnPage();
        });
        And ("^I'm on the annual bilateral StandardPermitsNoCabotage only submitted page$", () -> {
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
            NumberOfPermitsPageJourneySteps.completePage();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.BilateralDeclaration);
            DeclarationPageJourneySteps.completeDeclaration();
            AnnualBilateralJourney.getInstance()
                    .permitFee();

            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            SubmittedPage.untilOnPage();
        });
    }
}