package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.SubmittedPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;
import org.dvsa.testing.lib.newPages.external.pages.SubmittedPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ECMTPermitApplicationSteps.completeUpToCheckYourAnswersPage;
import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.newPages.Driver.DriverUtils.get;
import static org.dvsa.testing.lib.pages.BasePage.getElementValueByText;
import static org.junit.Assert.assertTrue;

public class ConfirmationPageSteps implements En {

    public ConfirmationPageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the Annual ECMT application submitted page$", () -> {
            clickToPermitTypePage(world);
            ECMTPermitApplicationSteps.completeEcmtApplicationConfirmation(operatorStore, world);
        });

        Then("^the reference number on the annual ECMT submitted page  is as expected$", () -> {
            SubmittedPage.untilOnPage();
            String actualReference = getElementValueByText("//div[@class='govuk-panel__body']", SelectorType.XPATH);
            assertTrue(actualReference.contains(operatorStore.getCurrentLicenceNumber().toString().substring(9, 18)));
        });

        Then("^all advisory texts on Annual ECMT submitted page is displayed correctly$", () -> {
            SubmittedPageJourney.hasPageHeading();
            SubmittedPageJourney.hasShortTermECMTAdvisoryText();
            assertTrue(SubmittedPage.isWarningMessagePresent());
        });

        Then("^I have an ongoing Annual ECMT with all fees paid", () -> {
            CommonSteps.clickToPermitTypePage(world);
            EcmtApplicationJourney.getInstance()
                    .permitType(PermitType.ECMT_ANNUAL, operatorStore);
            YearSelectionPage.selectECMTValidityPeriod();
            EcmtApplicationJourney.getInstance()
                    .licencePage(operatorStore, world);
            LicenceStore licenceStore = completeUpToCheckYourAnswersPage(world, operatorStore);
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "fees/").toString());

            HomePageJourney.payAllOutstandingFees();
            world.feeAndPaymentJourneySteps.customerPaymentModule();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "dashboard/").toString());
            HomePageJourney.selectPermitTab();

            HomePage.PermitsTab.selectFirstOngoingApplication();
            OverviewPageJourney.clickOverviewSection(OverviewSection.CheckYourAnswers);
            BasePermitPage.saveAndContinue();
            DeclarationPageJourney.completeDeclaration();
        });

        Then("^there shouldn't be a view receipt link on the Annual ECMT submitted page$", () -> {
            Assert.assertFalse(SubmittedPage.hasViewReceipt());
        });
    }
}
