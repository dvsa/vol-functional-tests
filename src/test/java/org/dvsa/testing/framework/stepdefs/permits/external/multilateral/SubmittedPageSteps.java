package org.dvsa.testing.framework.stepdefs.permits.external.multilateral;

import Injectors.World;
import activesupport.system.Properties;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualMultilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;
import org.dvsa.testing.lib.newPages.external.pages.SubmittedPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.dvsa.testing.lib.newPages.internal.BaseModel;
import org.dvsa.testing.lib.newPages.internal.details.FeesDetailsPage;
import org.dvsa.testing.lib.newPages.internal.details.enums.DetailsTab;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SubmittedPageSteps extends BasePermitPage implements En {
    public SubmittedPageSteps(OperatorStore operator, World world) {
        And("I am on the annual multilateral submitted page", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            completeMultilateralJourneyUntilDeclaration(operator, world);
            DeclarationPageJourney.completeDeclaration();
            world.feeAndPaymentJourneySteps.customerPaymentModule();
            SubmittedPage.untilOnPage();
        });
        Then("^the reference number on the multilateral submitted page is as expected$", () -> {
            SubmittedPage.untilOnPage();
            String actualReference = getElementValueByText("//div[@class='govuk-panel__body']", SelectorType.XPATH);
            assertEquals(actualReference.contains(operator.getCurrentLicenceNumber().toString().substring(9,18)),true);
        });
        And("^I have an ongoing annual multilateral with all fees paid$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            completeMultilateralJourneyUntilDeclaration(operator, world);

            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "fees/").toString());

            HomePageJourney.payAllOutstandingFees();

            world.feeAndPaymentJourneySteps.customerPaymentModule();
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true), "dashboard/").toString());
            HomePageJourney.selectPermitTab();

            HomePage.PermitsTab.selectFirstOngoingApplication();
            OverviewPageJourney.clickOverviewSection(OverviewSection.Declaration);
            DeclarationPageJourney.completeDeclaration();
        });
        Then("^there shouldn't be a view receipt link on the multilateral submitted page$", () -> {
            Assert.assertFalse(SubmittedPage.hasViewReceipt());
        });
        When("^a case worker worker pays all fees for my ongoing multilateral permit application$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            completeMultilateralJourneyUntilDeclaration(operator, world);

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            waitUntilElementIsEnabled("//a[@id='menu-licence_fees']",SelectorType.XPATH,60L,TimeUnit.SECONDS);
            IrhpPermitsPage.Tab.select(DetailsTab.Fees);


            //Pay Fee
            FeesDetailsPage.outstanding();
            FeesDetailsPage.pay();
            BaseModel.untilModalIsPresent(Duration.CENTURY, TimeUnit.SECONDS);
            IrhpPermitsApplyPage.selectCardPayment();
            world.feeAndPaymentJourneySteps.customerPaymentModule();
            FeesDetailsPage.untilFeePaidNotification();

            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            HomePage.PermitsTab.selectFirstOngoingApplication();
            OverviewPageJourney.clickOverviewSection(OverviewSection.Declaration);
       });
        When("^a case worker worker waives all fees for my ongoing multilateral permit application$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            completeMultilateralJourneyUntilDeclaration(operator, world);

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            untilElementIsPresent("//a[@id='menu-licence_fees']",SelectorType.XPATH,60L, TimeUnit.SECONDS);
            IrhpPermitsPage.Tab.select(DetailsTab.Fees);
            FeeDetailsPageJourney.whileFeesPresentWaveFee();
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            HomePage.PermitsTab.selectFirstOngoingApplication();
            OverviewPageJourney.clickOverviewSection(OverviewSection.Declaration);
        });
        Then("^all the multilateral submitted advisory text is present$", () -> {
            String heading = SubmittedPage.getSubHeading();
            assertEquals("What happens next", heading);
            assertTrue(isTextPresent("Your valid permits will be grouped together under the same licence number that you applied with."));
            assertTrue(isTextPresent("We will now post your paper permit and corresponding logbook within the next 3 working days."));
        });
    }

    private void completeMultilateralJourneyUntilDeclaration(OperatorStore operator, World world) {
        HomePageJourney.beginPermitApplication();
        AnnualMultilateralJourney.INSTANCE
                .permitType(PermitType.ANNUAL_MULTILATERAL, operator)
                .licencePage(operator, world)
                .overviewPage(OverviewSection.NumberOfPaymentsRequired, operator);
        NumberOfPermitsPageJourney.completeMultilateralPage();
        AnnualMultilateralJourney.INSTANCE
                .checkYourAnswers();
    }
}
