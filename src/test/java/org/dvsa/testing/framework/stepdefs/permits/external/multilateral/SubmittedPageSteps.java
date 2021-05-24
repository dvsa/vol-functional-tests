package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import Injectors.World;
import activesupport.string.Str;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.FeePaymentConfirmationPage;
import org.dvsa.testing.lib.pages.external.permit.PayFeesPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.ReceiptPage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.ApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.OverviewPage;
import org.dvsa.testing.lib.pages.internal.BaseModel;
import org.dvsa.testing.lib.pages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import static org.dvsa.testing.lib.pages.BasePage.*;

public class SubmittedPageSteps implements En {
    public SubmittedPageSteps(OperatorStore operator, World world) {
        And("I am on the annual multilateral submitted page", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            AnnualMultilateralJourney.INSTANCE
                    .beginApplication()
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                    .licencePage(operator, world)
                    .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator)
                    .numberOfPermitsPage(operator)
                    .checkYourAnswers()
                    .declaration(true)
                    .feeOverviewPage()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();

            ApplicationSubmitPage.untilOnPage();
        });
        Then("^the reference number on the multilateral submitted page is as expected$", () -> {
            ApplicationSubmitPage.untilOnSubmittedPage();
            String actualReference = getElementValueByText("//div[@class='govuk-panel__body']", SelectorType.XPATH);
            Assert.assertEquals(actualReference.contains(operator.getCurrentLicenceNumber().toString().substring(9,18)),true);
        });

        When("I select view receipt from application submitted page", () -> {
            WebDriver driver = getDriver();
            ApplicationSubmitPage.receipt();
            String[] windows = driver.getWindowHandles().toArray(new String[0]);
            driver.switchTo().window(windows[1]);
            ReceiptPage.untilOnPage();
        });
        And("^I have an ongoing annual multilateral with all fees paid$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            AnnualMultilateralJourney.INSTANCE
                    .beginApplication()
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                    .licencePage(operator, world)
                    .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator)
                    .numberOfPermitsPage(operator)
                    .checkYourAnswers();

            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "fees/").toString());

            HomePage.FeesTab.outstanbding(true);
            HomePage.FeesTab.pay();
            PayFeesPage.payNow();

            AnnualMultilateralJourney.INSTANCE.cardDetailsPage().cardHolderDetailsPage().confirmAndPay();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "dashboard/").toString());
            HomePage.selectTab(Tab.PERMITS);

            HomePage.PermitsTab.selectOngoing(operator.getCurrentLicence().get().getReferenceNumber());
            OverviewPage.select(OverviewSection.Declaration);
            AnnualMultilateralJourney.INSTANCE.declaration(true);
        });
        Then("^there shouldn't be a view receipt link on the multilateral submitted page$", () -> {
            Assert.assertFalse(ApplicationSubmitPage.hasViewReceipt());
        });
        When("^a case worker worker pays all fees for my ongoing multilateral permit application$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            AnnualMultilateralJourney.INSTANCE
                    .beginApplication()
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                    .licencePage(operator, world)
                    .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator)
                    .numberOfPermitsPage(operator)
                    .checkYourAnswers();

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            waitUntilElementIsEnabled("//a[@id='menu-licence_fees']",SelectorType.XPATH,60L,TimeUnit.SECONDS);
            IrhpPermitsPage.Tab.select(BaseDetailsPage.DetailsTab.Fees);


            //Pay Fee
            FeesDetailsPage.outstanding();
            FeesDetailsPage.pay();
            BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
            IrhpPermitsApplyPage.selectCardPayment();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage();
            FeePaymentConfirmationPage.makeMayment();
            FeesDetailsPage.untilFeePaidNotification();

            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());            HomePage.selectTab(Tab.PERMITS);
            HomePage.PermitsTab.selectOngoing(operator.getCurrentLicence().get().getReferenceNumber());
            OverviewPage.select(OverviewSection.Declaration);
       });
        When("^a case worker worker waives all fees for my ongoing multilateral permit application$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            AnnualMultilateralJourney.INSTANCE
                    .beginApplication()
                    .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                    .licencePage(operator, world)
                    .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator)
                    .numberOfPermitsPage(operator)
                    .checkYourAnswers();

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            untilElementIsPresent("//a[@id='menu-licence_fees']",SelectorType.XPATH,60L, TimeUnit.SECONDS);
            IrhpPermitsPage.Tab.select(BaseDetailsPage.DetailsTab.Fees);

            while(FeesDetailsPage.hasFee()) {
                FeesDetailsPage.select1stFee();
                FeesDetailsPage.waive(true);
                FeesDetailsPage.waiveNote(Str.randomWord(180));
                FeesDetailsPage.recommandWaiver();
                FeesDetailsPage.waive(FeesDetailsPage.Decision.Approve);
            }

            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());            HomePage.selectTab(Tab.PERMITS);
            HomePage.PermitsTab.selectOngoing(operator.getCurrentLicence().get().getReferenceNumber());
            OverviewPage.select(OverviewSection.Declaration);
        });
        Then("^all the multilateral submitted advisory text is present$", () -> {
            Assert.assertTrue(ApplicationSubmitPage.hasAdvisoryText());
        });
    }
}
