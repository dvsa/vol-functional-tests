package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import activesupport.system.Properties;
import io.cucumber.java8.En;;
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
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
            assertFalse(SubmittedPage.hasViewReceipt());
        });
    }
}