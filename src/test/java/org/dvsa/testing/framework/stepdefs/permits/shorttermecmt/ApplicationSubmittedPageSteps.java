package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import activesupport.string.Str;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.ECMTShortTermJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.DeclarationPage;
import org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyProportion;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitUsage;
import org.dvsa.testing.lib.pages.external.permit.enums.Sector;
import org.dvsa.testing.lib.pages.external.permit.multilateral.ApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CabotagePage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CertificatesRequiredPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.OverviewPage;
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

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.external.permit.BaseOverviewPage.untilOnPage;
import static org.dvsa.testing.lib.pages.external.permit.OverviewPage.section;
import static org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CabotagePage.cabotageConfirmation;
import static org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CertificatesRequiredPage.CertificatesRequiredConfirmation;

public class ApplicationSubmittedPageSteps extends BasePage implements En {

    public ApplicationSubmittedPageSteps(OperatorStore operatorStore, World world)
    {
        And ("^I am on the application submitted page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitTypePage.PermitType.ShortTermECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            OverviewPage.select(OverviewPage.Section.HowwillyouusethePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            CabotagePage.cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.CertificatesRequiredConfirmation();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            NumberOfPermitsPage.euro5OrEuro6permitsValue();
            BasePermitPage.saveAndContinue();
            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();
            AnnualTripsAbroadPage.quantity(10);
            BasePermitPage.saveAndContinue();
            ProportionOfInternationalJourneyPage.proportion(JourneyProportion.LessThan60Percent);
            SectorPage.sector(Sector.random());
            ECMTShortTermJourney.getInstance().checkYourAnswersPage();
            DeclarationPage.declare(true);
            DeclarationPage.saveAndContinue();
            PermitFee.submitAndPay();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
        });
        Then("^the reference number on the short term ECMT submitted page  is as expected$", () -> {
            SubmittedPage.untilElementIsPresent("//h1[@class='govuk-panel__title']",SelectorType.XPATH,10,TimeUnit.SECONDS);
            String actualReference = getElementValueByText("//div[@class='govuk-panel__body'] ", SelectorType.XPATH);
            Assert.assertEquals(actualReference.contains(operatorStore.getCurrentLicenceNumber().toString().substring(9,18)),true);
        });
        When ("^a case worker worker pays all fees for my ongoing short term  permit application$", () -> {
            CommonSteps.clickToPermitTypePage(world);
            ECMTShortTermJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.ShortTermECMT,operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            org.dvsa.testing.lib.pages.external.permit.shorttermecmt.OverviewPage.select(org.dvsa.testing.lib.pages.external.permit.shorttermecmt.OverviewPage.Section.HowwillyouusethePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredConfirmation();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage.euro5OrEuro6permitsValue();
            BasePermitPage.saveAndContinue();
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            refreshPage();
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

            ShorttermECMTJourney.getInstance().go(ApplicationType.EXTERNAL);
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);

            String licence = operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence);
            section(PermitSection.EuroEmissionsStandards);
            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();
            AnnualTripsAbroadPage.quantity(10);
            BasePermitPage.saveAndContinue();
            ProportionOfInternationalJourneyPage.proportion(JourneyProportion.LessThan60Percent);
            SectorPage.sector(Sector.random());
            ECMTShortTermJourney.getInstance().checkYourAnswersPage();
            DeclarationPage.declare(true);
            DeclarationPage.saveAndContinue();
        });
        Then("^a case worker waives all fees for my ongoing short term permit application$", () -> {
            CommonSteps.clickToPermitTypePage(world);
            ECMTShortTermJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.ShortTermECMT,operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            org.dvsa.testing.lib.pages.external.permit.shorttermecmt.OverviewPage.select(org.dvsa.testing.lib.pages.external.permit.shorttermecmt.OverviewPage.Section.HowwillyouusethePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredConfirmation();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage.euro5OrEuro6permitsValue();
            BasePermitPage.saveAndContinue();
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            waitUntilElementIsEnabled("//a[@id='menu-licence_fees']",SelectorType.XPATH,60L,TimeUnit.SECONDS);
            IrhpPermitsPage.Tab.select(BaseDetailsPage.DetailsTab.Fees);

            while(FeesDetailsPage.hasFee()) {
                FeesDetailsPage.select1stFee();
                FeesDetailsPage.waive(true);
                FeesDetailsPage.waiveNote(Str.randomWord(180));
                FeesDetailsPage.recommandWaiver();
                FeesDetailsPage.waive(FeesDetailsPage.Decision.Approve);
            }
            ShorttermECMTJourney.getInstance().go(ApplicationType.EXTERNAL);
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);

            String licence = operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence);
               untilOnPage();
            section(PermitSection.EuroEmissionsStandards);

            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();
            AnnualTripsAbroadPage.quantity(10);
            BasePermitPage.saveAndContinue();
            ProportionOfInternationalJourneyPage.proportion(JourneyProportion.LessThan60Percent);
            SectorPage.sector(Sector.random());
            ECMTShortTermJourney.getInstance().checkYourAnswersPage();
            DeclarationPage.declare(true);
            DeclarationPage.saveAndContinue();
        });
        Then("^there shouldn't be a view receipt link on the shortterm ECMT submitted page$", () -> {
            Assert.assertFalse(ApplicationSubmitPage.hasViewReceipt());
        });
        And ("^all advisory texts on short term ECMT submitted page is displayed correctly$", () -> {
            SubmittedPage.pageHeading();
            SubmittedPage.advisoryText();
            SubmittedPage.warningMessage();
        });
        Then("^I select view receipt from short term application submitted page$", BaseApplicationSubmitPage::receipt);
        Then("^the view receipt hyperlink opens in a new window$", () -> {
            WebDriver driver = getDriver();
            String[] windows = driver.getWindowHandles().toArray(new String[0]);
            driver.switchTo().window(windows[1]);
            ReceiptPage.untilOnPage();
            driver.switchTo().window(windows[0]);
        });
        And("^I have an ongoing short term ECMT with all fees paid$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitTypePage.PermitType.ShortTermECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            OverviewPage.select(OverviewPage.Section.HowwillyouusethePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            cabotageConfirmation();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredConfirmation();
            BasePermitPage.saveAndContinue();
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            org.dvsa.testing.lib.pages.external.permit.NumberOfPermitsPage.euro5OrEuro6permitsValue();
            BasePermitPage.saveAndContinue();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "fees/").toString());
            HomePage.FeesTab.outstanbding(true);
            HomePage.FeesTab.pay();
            PayFeesPage.payNow();

            AnnualMultilateralJourney.INSTANCE.cardDetailsPage().cardHolderDetailsPage().confirmAndPay();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "dashboard/").toString());
            HomePage.selectTab(Tab.PERMITS);

            String licence = operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence);
            section(PermitSection.EuroEmissionsStandards);
            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();
            AnnualTripsAbroadPage.quantity(10);
            BasePermitPage.saveAndContinue();
            ProportionOfInternationalJourneyPage.proportion(JourneyProportion.LessThan60Percent);
            SectorPage.sector(Sector.random());
            ECMTShortTermJourney.getInstance().checkYourAnswersPage();
            DeclarationPage.declare(true);
            DeclarationPage.saveAndContinue();
        });
    }
}
