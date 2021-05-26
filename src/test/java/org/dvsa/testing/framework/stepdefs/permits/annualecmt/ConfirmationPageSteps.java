package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import activesupport.system.Properties;
import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.multilateral.ApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.SubmittedPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.getElementValueByText;
import static org.dvsa.testing.lib.newPages.Driver.DriverUtils.get;
import static org.dvsa.testing.lib.newPages.Driver.DriverUtils.getDriver;

public class ConfirmationPageSteps implements En {

    public ConfirmationPageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the Annual ECMT application submitted page$", () -> {
            clickToPermitTypePage(world);
            ECMTPermitApplicationSteps.completeEcmtApplicationConfirmation(operatorStore, world);
        });

        Then("^the reference number on the annual ECMT submitted page  is as expected$", () -> {
BaseApplicationSubmitPage.untilSubmittedPageLoad();
            String actualReference = getElementValueByText("//div[@class='govuk-panel__body']", SelectorType.XPATH);
            Assert.assertEquals(actualReference.contains(operatorStore.getCurrentLicenceNumber().toString().substring(9,18)),true);
             });

        Then("^all advisory texts on Annual ECMT submitted page is displayed correctly$", () -> {
            SubmittedPage.pageHeading();
            SubmittedPage.advisoryText();
            SubmittedPage.warningMessage();
               });

        Then("^I select view receipt from Annual ECMT application submitted page$", BaseApplicationSubmitPage::receipt);

        Then("^the view receipt of Annual ECMT hyperlink opens in a new window$", () -> {
            WebDriver driver = getDriver();
            String[] windows = driver.getWindowHandles().toArray(new String[0]);
            driver.switchTo().window(windows[1]);
            ReceiptPage.untilOnPage();
            driver.switchTo().window(windows[0]);
                 });

        Then("^I have an ongoing Annual ECMT with all fees paid", () -> {
            CommonSteps.clickToPermitTypePage(world);
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitType.ECMT_ANNUAL, operatorStore);
            YearSelectionPage.EcmtValidityPeriod();
            EcmtApplicationJourney.getInstance()
                    .licencePage(operatorStore, world);
            LicenceStore licenceStore = completeUpToCheckYourAnswersPage(world, operatorStore);
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "fees/").toString());

            HomePage.FeesTab.outstanbding(true);
            HomePage.FeesTab.pay();
            PayFeesPage.payNow();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "dashboard/").toString());
            HomePage.selectTab(Tab.PERMITS);

            String licence = operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.CheckYourAnswers);
            BasePermitPage.saveAndContinue();
            DeclarationPage.declare(true);
            DeclarationPage.saveAndContinue();

        });

        Then("^there shouldn't be a view receipt link on the Annual ECMT submitted page$", () -> {
            Assert.assertFalse(ApplicationSubmitPage.hasViewReceipt());
        });
    }
}
