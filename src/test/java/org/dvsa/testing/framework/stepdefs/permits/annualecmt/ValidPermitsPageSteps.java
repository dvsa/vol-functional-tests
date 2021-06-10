package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.PermitApplication;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.ValidPermit.ValidAnnualMultilateralPermit;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.SubmittedPage;
import org.dvsa.testing.lib.newPages.permits.pages.bilateralsOnly.ValidAnnualBilateralPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.multilateralsOnly.ValidAnnualMultilateralPermitsPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;

public class ValidPermitsPageSteps extends BasePage implements En {

    public static Map<String, PermitApplication> userPermitsSelected = new HashMap();

    public ValidPermitsPageSteps(OperatorStore operatorStore, World world) {
        And("^have valid permits$", () -> {
            // TODO: replace steps to issue permits with an API call should devs bother documenting it
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            ECMTPermitApplicationSteps.completeEcmtApplication(operatorStore, world);
            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            operatorStore.setCurrentLicenceNumber(licence.getLicNo());

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            IrhpPermitsApplyPage.licence();
            String browser = String.valueOf(getURL());
            get(browser+"irhp-application/");
            IrhpPermitsApplyPage.viewApplication();
            IrhpPermitsApplyPage.grantApplication();
            IrhpPermitsApplyPage.continueButton();
            sleep(5000);
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true)).toString());
            waitForTitleToBePresent("Sign in to your Vehicle Operator Licensing account");
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            refreshPage();
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);

            // select one at random
            String licence1= licence.getLicNo();
            HomePage.PermitsTab.selectOngoing(licence1);
            ApplicationIssuingFeePage.acceptAndPay();

            // pay for application
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay()
                    .passwordAuthorisation();
            SubmittedPage.untilOnPage();
            SubmittedPage.goToPermitsDashboard();

            // Refresh until permit is valid
            untilAnyPermitStatusMatch(PermitStatus.VALID);
            HomePage.PermitsTab.select(licence1);

        });
        And("^have valid Annual Bilateral Permits$", () -> {
            HomePage.applyForLicenceButton();
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore);
            AnnualBilateralJourney.getInstance().licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance()
                    .overview(OverviewSection.Countries)
                    .countries(operatorStore)
                    .numberOfPermits(operatorStore)
                    .checkYourAnswers()
                    .declare(true)
                    .permitFee();

            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay();
        });
        Then("^the user is in the annual ECMT list page$",()  ->{
            Assert.assertTrue(isPath("/permits/valid/\\d+"));
            String title = BasePage.getElementValueByText("h1.govuk-heading-l",SelectorType.CSS).trim();
            Assert.assertEquals("Annual ECMT", title);
        });
        And ("^I select returns to permit dashboard hyperlink", ValidAnnualBilateralPermitsPage::permitDashboard);
        Then ("^the licence number is displayed above the page heading",  () ->{
            String expectedReference= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            Assert.assertEquals(expectedReference, BasePermitPage.getReferenceFromPage());
        });
        Then ("^the ECMT application licence number is displayed above the page heading",  () ->{
            String expectedReference= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            String actual = BasePermitPage.getElementValueByText("//span[@class='govuk-caption-xl']", SelectorType.XPATH);
            Assert.assertEquals(expectedReference, actual);
        });
        Then("^the ECMT permit list page table should display all relevant fields$", () -> {
            String message = "Expected all permits to have a status of 'valid'";
            OperatorStore store = operatorStore;
            List<ValidAnnualMultilateralPermit> permits = ValidAnnualMultilateralPermitsPage.permits();
            Assert.assertTrue(message, permits.stream().allMatch(permit -> permit.getStatus() == PermitStatus.VALID));
            IntStream.range(0, permits.size() - 1).forEach((idx) -> Assert.assertTrue(
                    permits.get(idx).getExpiryDate().isBefore(permits.get(idx + 1).getExpiryDate()) ||
                            permits.get(idx).getExpiryDate().isEqual(permits.get(idx + 1).getExpiryDate())
            ));
        });
        When ("^the user is on self-serve permits dashboard",  () ->{
            HomePage.selectTab(Tab.PERMITS);
        });
    }

    public static void untilAnyPermitStatusMatch(PermitStatus status) {
        untilPermitStatusIsNot(HomePage.PermitsTab::anyPermitWithStatus, status );
    }

    private static <T> void untilPermitStatusIsNot(Predicate<T> p, T status) {
        int maxTries = 30;

        while(!p.test(status) && maxTries > 0) {
            refreshPage();

            if (p.test(status))
                break;

            try {
                TimeUnit.SECONDS.sleep(Duration.CENTURY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            maxTries--;
        }

        if (maxTries <= 0 && !p.test(status))
            throw new RuntimeException("Permit status did not meet desired criterion state");
    }
}
