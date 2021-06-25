package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.newPages.external.pages.SubmittedPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class SubmissionPageSteps extends BasePermitPage implements En {

    public SubmissionPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the ECMT International removal submission page", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
                    .licencePage(operatorStore, world);
            OverviewPageJourney.clickOverviewSection(OverviewSection.RemovalsEligibility);
            EcmtInternationalRemovalJourney.getInstance()
                    .removalsEligibility(true)
                    .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage();
            NumberOfPermitsPageJourney.completePage();
            EcmtInternationalRemovalJourney.getInstance()
                    .checkYourAnswers();
            DeclarationPageJourney.completeDeclaration();
            world.feeAndPaymentJourneySteps.customerPaymentModule();
            EcmtApplicationJourney.getInstance()
                    .feeOverviewPage();
            SubmittedPage.untilOnPage();
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
        And ("^the texts on the submission page are displayed correctly", () -> {
            String expectedHeading = SubmittedPage.getSubHeading();
            String expectedAdvisoryText1 = getText("//p[contains(text(),'We will now post your paper permit within the next')]", SelectorType.XPATH);
            String expectedAdvisoryText2 = getText("//p[contains(text(),'Your valid permits will be grouped together under')]", SelectorType.XPATH);
            String expectedWarningMessage = getText("//strong[@class='govuk-warning-text__text']", SelectorType.XPATH);

            Assert.assertEquals("What happens next", expectedHeading);
            Assert.assertEquals("We will now post your paper permit within the next 3 working days.", expectedAdvisoryText1);
            Assert.assertEquals("Your valid permits will be grouped together under the same licence number that you applied with.", expectedAdvisoryText2);
            Assert.assertEquals("Warning" +"\n"+"Make sure your correspondence address is correct on all your operator licences and your email address is up-to-date on your account.", expectedWarningMessage);
        });
        Then ("^the view receipt of ECMT International hyperlink opens in a new window", () -> {
            WebDriver driver = getDriver();
            String[] windows = driver.getWindowHandles().toArray(new String[0]);
            SubmittedPage.openReceipt();
            driver.switchTo().window(windows[0]);
        });
        And ("^I have partial ECMT international removal application", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
                    .licencePage(operatorStore, world);
            OverviewPageJourney.clickOverviewSection(OverviewSection.RemovalsEligibility);
            EcmtInternationalRemovalJourney.getInstance()
                    .removalsEligibility(true)
                    .cabotagePage()
                    .certificatesRequiredPage()
                    .permitStartDatePage();
            NumberOfPermitsPageJourney.completePage();
            EcmtInternationalRemovalJourney.getInstance()
                    .checkYourAnswers();
        });
        And ("^the application is under issued permits table with status as valid", () -> {
            refreshPage();
            untilAnyPermitStatusMatch(PermitStatus.VALID);
        });
        And ("^I navigate to permit dashboard page", () -> {
            world.selfServeNavigation.navigateToNavBarPage("home");
        });
        And ("^I'm on the ECMT international submitted page for my active application", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            clickByLinkText(operatorStore.getLatestLicence().get().getEcmt().getReferenceNumber());
            OverviewPageJourney.clickOverviewSection(OverviewSection.Declaration);
            DeclarationPageJourney.completeDeclaration();
        });
        And ("^I proceed with the application", () -> {
            HomePage.selectTab(Tab.PERMITS);
            String licence1= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
            OverviewPageJourney.clickOverviewSection(OverviewSection.Declaration);
            DeclarationPageJourney.completeDeclaration();
        });

    }
}