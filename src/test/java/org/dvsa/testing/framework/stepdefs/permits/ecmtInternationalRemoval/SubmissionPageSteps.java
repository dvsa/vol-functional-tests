package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.BaseJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.Driver.DriverUtils;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.BaseApplicationSubmitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.SubmissionPage;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.signIn;

public class SubmissionPageSteps extends DriverUtils implements En {

    public SubmissionPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the ECMT International removal submission page", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.EcmtInternationalRemoval, operatorStore)
                    .licencePage(operatorStore, world);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewPage.Section.RemovalsEligibility, operatorStore)
                    .removalsEligibility(true)
                    .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage()
                    .numberOfPermits()
                    .checkYourAnswers()
                    .declaration();
            EcmtApplicationJourney.getInstance()
                    .feeOverviewPage()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay()
                    .passwordAuthorisation();
            BaseApplicationSubmitPage.untilSubmittedPageLoad();
        });
        Then ("^the page heading on the submission page is displayed correctly", () -> {

            Assert.assertEquals(BasePage.getElementValueByText("//h1[@class='govuk-panel__title']", SelectorType.XPATH),"Application submitted");
        });
        And ("^the application reference number is displayed correctly", () -> {
            String referenceNumber=BasePage.getElementValueByText("//div[@class='govuk-panel__body']",SelectorType.XPATH);
            Assert.assertTrue(referenceNumber.contains("Your reference number"));
            String expectedLicenceNumber= operatorStore.getCurrentLicenceNumber().orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber= BasePage.getElementValueByText("//div/strong",SelectorType.XPATH);
            Assert.assertTrue(actualReferenceNumber.contains(expectedLicenceNumber));
        });
        And ("^the texts on the submission page are displayed correctly", SubmissionPage::guidelineText);
        Then ("^the view receipt of ECMT International hyperlink opens in a new window", () -> {
            WebDriver driver = getDriver();
            String[] windows = driver.getWindowHandles().toArray(new String[0]);
            SubmissionPage.viewReceiptLink();
            driver.switchTo().window(windows[0]);
        });
        And ("^I have partial ECMT international removal application", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.EcmtInternationalRemoval, operatorStore)
                    .licencePage(operatorStore, world);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewPage.Section.RemovalsEligibility, operatorStore)
                    .removalsEligibility(true)
                    .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage()
                    .numberOfPermits()
                    .checkYourAnswers();
        });
        And ("^the application is under issued permits table with status as valid", () -> {
            refreshPage();
            untilAnyPermitStatusMatch(PermitStatus.VALID);
        });
        And ("^I navigate to permit dashboard page", SubmissionPage::homeButton);
        And ("^I'm on the ECMT international submitted page for my active application", () -> {
            BaseJourney.getInstance().go(ApplicationType.EXTERNAL);
            signIn(world);
            HomePage.selectTab(Tab.PERMITS);
            get(org.dvsa.testing.lib.pages.external.permit.bilateral.OverviewPage.url(operatorStore.getLatestLicence().get().getEcmt().getReferenceNumber()));
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewPage.Section.Declaration, operatorStore)
                    .declare(true);
        });
        And ("^I proceed with the application", () -> {
            HomePage.selectTab(Tab.PERMITS);
            String licence1= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewPage.Section.Declaration, operatorStore)
                    .declare(true);
        });

    }
}