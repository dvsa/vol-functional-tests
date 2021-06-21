package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import activesupport.number.Int;
import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.BaseInternalJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.PermitApplication;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.bilateral.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.ValidAnnualBilateralPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.multilateral.ValidAnnualMultilateralPermitsPage;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;

public class ValidPermitsPageSteps extends BasePage implements En {

    public ValidPermitsPageSteps(OperatorStore operatorStore, World world) {
        And("^have valid permits$", () -> {
            // TODO: replace steps to issue permits with an API call should devs bother documenting it
            CommonSteps.signInAndAcceptCookies(world);
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            ECMTPermitApplicationSteps.completeEcmtApplication(operatorStore, world);
            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            operatorStore.setCurrentLicenceNumber(licence.getLicNo());

            BaseInternalJourney.getInstance().openLicence(
                    licence.getLicenceId()
            ).signin();
            IrhpPermitsApplyPage.licence();
            String browser = String.valueOf(getURL());
            get(browser+"irhp-application/");
            IrhpPermitsApplyPage.viewApplication();
            IrhpPermitsApplyPage.grantApplication();
            IrhpPermitsApplyPage.continueButton();
            sleep(5000);
            get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true)).toString());
            waitForTextToBePresent("Sign in to your Vehicle Operator Licensing account                ");
            LoginPage.signIn(world.get("username"), world.get("password"));
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
            BaseApplicationSubmitPage.untilSubmittedPageLoad();
            BaseApplicationSubmitPage.finish();

            // Refresh until permit is valid
            untilAnyPermitStatusMatch(PermitStatus.VALID);
            HomePage.PermitsTab.select(licence1);

        });
        And("^have valid Annual Bilateral Permits$", () -> {
            HomePage.applyForLicenceButton();
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore);
            AnnualBilateralJourney.getInstance().licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance()
                    .overview(OverviewPage.Section.Countries, operatorStore)
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

        When("^I select the permit number$", () -> {
            PermitApplication validPermit = HomePage.PermitsTab.permitsWithStatus(HomePage.PermitsTab.Table.issued, PermitStatus.VALID).get(0);
            HomePage.PermitsTab.select(validPermit.getReferenceNumber());
            world.put("user.permit.selected", validPermit);
        });
        Then("^all the displayed information should be correct$", () -> {
            PermitApplication application = world.get("user.permit.selected");

            Assert.assertThat(Permits.permitStatus(), is(application.getStatus()));

            // Checks the number of permits matches the number of permits that were displayed on the home page.
            Assert.assertThat(Permits.numberOfPermits(), is(application.getNoOfPermits()));
            // Checks the number of permits in the permits page matches the number of permits in the table of permits.
            Assert.assertThat(Permits.numberOfPermits(), is(Permits.listPermits().size()));
        });
        Then("^I can see the Annual Bilateral Permit applications above ECMT Annual Permit applications$", () -> {

        });
        And ("^I select returns to permit dashboard hyperlink", ValidAnnualBilateralPermitsPage::permitDashboard);
        Then ("^the licence number is displayed above the page heading",  () ->{
            String expectedReference= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            String actual = ValidAnnualMultilateralPermitsPage.reference();
            Assert.assertEquals(expectedReference, ValidAnnualMultilateralPermitsPage.reference());
        });
        Then ("^the ECMT application licence number is displayed above the page heading",  () ->{
            String expectedReference= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            String actual = BasePermitPage.getElementValueByText("//span[@class='govuk-caption-xl']", SelectorType.XPATH);
            Assert.assertEquals(expectedReference, actual);
        });
        Then("^the ECMT permit list page table should display all relevant fields$", () -> {
            String message = "Expected all permits to have a status of 'valid'";
            OperatorStore store = operatorStore;
            List<ValidAnnualMultilateralPermitsPage.Permit> permits = ValidAnnualMultilateralPermitsPage.permits();
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

    public static void untilNonPermitStatusMatch(PermitStatus status) {
        untilPermitStatusIsNot(HomePage.PermitsTab::nonePermitWithStatus, status);
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

    private static PermitApplication selectRandomPermit(List<PermitApplication> ongoingPermits) {
        int index = Int.random(0, ongoingPermits.size() - 1);
        return ongoingPermits.get(index);
    }

}
