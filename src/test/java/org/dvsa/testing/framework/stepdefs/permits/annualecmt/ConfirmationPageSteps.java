package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import activesupport.system.Properties;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.SubmittedPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;
import org.dvsa.testing.lib.newPages.external.pages.SubmittedPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.DeclarationPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.SubmittedPageJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class ConfirmationPageSteps extends BasePage implements En {

    public ConfirmationPageSteps(World world) {

        Then("^I am on the Annual ECMT application submitted page$", () -> {
            EcmtApplicationJourney.completeEcmtApplicationConfirmation(world);
        });

        Then("^the reference number on the annual ECMT submitted page  is as expected$", () -> {
            SubmittedPage.untilOnPage();
            String actualReference = getElementValueByText("//div[@class='govuk-panel__body']", SelectorType.XPATH);
            assertTrue(actualReference.contains(world.applicationDetails.getLicenceNumber()));
        });

        Then("^all advisory texts on Annual ECMT submitted page is displayed correctly$", () -> {
            SubmittedPageJourney.hasPageHeading();
            SubmittedPageJourney.hasShortTermECMTAdvisoryText();
        });

        Then("^I have an ongoing Annual ECMT with all fees paid", () -> {
            EcmtApplicationJourney.beginApplication(world);
            EcmtApplicationJourney.completeUntilCheckYourAnswersPage();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "fees/").toString());

            HomePageJourney.payAllOutstandingFees();
            world.feeAndPaymentJourney.customerPaymentModule();
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
