package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.BaseJourney;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.newPages.permits.BilateralJourneySteps;
import org.dvsa.testing.lib.newPages.permits.pages.EssentialInformationPage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BaseApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.ApplicationSubmitPage;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.getElementValueByText;

public class AnnualBilateralSubmittedPageSteps implements En {
    public AnnualBilateralSubmittedPageSteps(LicenceStore licenceStore, OperatorStore operatorStore, World world) {
        And("^I'm on the annual bilateral cabotage only submitted  page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BilateralJourneySteps.clickYesToCabotage();
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.numberOfPermits();
            BasePermitPage.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.selectDeclaration();
            AnnualBilateralJourney.getInstance().declare(true)
                    .permitFee();

            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            BaseApplicationSubmitPage.untilSubmittedPageLoad();
        });
        Then("^my application reference should be displayed$", () -> {
            String actualReference = getElementValueByText("//div[@class='govuk-panel__body'] ", SelectorType.XPATH);
            Assert.assertEquals(actualReference.contains(operatorStore.getCurrentLicenceNumber().toString().substring(9,18)),true);
        });
        Then("^I should see the view receipt link$", () -> {
            Assert.assertTrue("'View  Receipt' link  should be displayed but wasn't", ApplicationSubmitPage.hasViewReceipt());
        });
        And("^I'm on the annual bilateral submitted page for my active application$", () -> {
            BaseJourney.getInstance().go(ApplicationType.EXTERNAL);
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            String licence1 = operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
            OverviewPage.selectDeclaration();
            AnnualBilateralJourney.getInstance().declare(true);
        });
        Then("^I should not see the view receipt link$", () -> {
            Assert.assertFalse("'View  Receipt' link  should NOT be displayed but was", ApplicationSubmitPage.hasViewReceipt()
            );
        });
        When("^I select finish$", ApplicationSubmitPage::finish);
        And ("^I'm on the annual bilateral StandardAndCabotagePermits only submitted page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
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
            AnnualBilateralJourney.getInstance().declare(true)
                    .permitFee();

            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            BaseApplicationSubmitPage.untilSubmittedPageLoad();
        });
        And ("^I'm on the annual bilateral StandardPermitsNoCabotage only submitted page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralsStandardPermitsNoCabotage,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
            AnnualBilateralJourney.getInstance().journeyType(world, licenceStore);
            BasePermitPage.saveAndContinue();
            NumberOfPermitsPage.numberOfPermits();
            BasePermitPage.saveAndContinue();
            BasePermitPage.waitAndClick("//input[@id='submitbutton']", SelectorType.XPATH);
            OverviewPage.selectDeclaration();
            AnnualBilateralJourney.getInstance().declare(true)
                    .permitFee();

            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            BaseApplicationSubmitPage.untilSubmittedPageLoad();
        });
    }
}