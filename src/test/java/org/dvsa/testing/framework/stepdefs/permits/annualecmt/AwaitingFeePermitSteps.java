package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import activesupport.IllegalBrowserException;
import activesupport.config.Configuration;
import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import com.typesafe.config.Config;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.common.TimeUtils;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.PermitApplication;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.AdminOption;
import org.dvsa.testing.lib.pages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.exception.ElementDidNotAppearWithinSpecifiedTimeException;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.ApplicationIssuingFeePage;
import org.dvsa.testing.lib.pages.internal.admin.permits.BaseAdminPermits;
import org.dvsa.testing.lib.pages.internal.admin.permits.Permit;
import org.dvsa.testing.lib.pages.internal.admin.permits.Scoring;
import org.dvsa.testing.lib.pages.internal.admin.permits.Window;
import org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.Assert;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;
import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.dvsa.testing.lib.pages.external.permit.ApplicationIssuingFeePage.ApplicationInfo.*;
import static org.hamcrest.core.Is.is;

public class AwaitingFeePermitSteps extends BasePage implements En {

    private World world;

    public AwaitingFeePermitSteps(OperatorStore operatorStore, World world) {

        And("^I am viewing an application that's awaiting fees$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            HomePage.applyForLicenceButton();
            ECMTPermitApplicationSteps.completeEcmtApplication(operatorStore, world);
            LicenceModel licence = OrganisationAPI.dashboard(operatorStore.getOrganisationId()).getDashboard().getLicences().get(0);
            operatorStore.setCurrentLicenceNumber(licence.getLicNo());

            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            IrhpPermitsApplyPage.licence();
            String browser = String.valueOf(getURL());
            getDriver().get(browser+"irhp-application/");
            IrhpPermitsApplyPage.viewApplication();
            IrhpPermitsApplyPage.grantApplication();
            IrhpPermitsApplyPage.continueButton();
            sleep(3000);
            getDriver().get(URL.build(ApplicationType.EXTERNAL, Properties.get("env", true)).toString());
            waitForTextToBePresent("Sign in to your Vehicle Operator Licensing account                ");
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePage.selectTab(Tab.PERMITS);
            getDriver().navigate().refresh();
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
            String licence1= operatorStore.getCurrentLicenceNumber().toString().substring(9,18);
            HomePage.PermitsTab.selectOngoing(licence1);
        });
        When("^I cancel and return to dashboard from issuing fee page$", ApplicationIssuingFeePage::cancelAndReturnToDashboard);
        When("^I decline payment$", ApplicationIssuingFeePage::declinePermits);
        Then("^I should be on the decline awarded permits page$", () -> Assert.assertTrue(isPath("permits/application/\\d+/decline/")));
        When("^I accept and pay the issuing fee$", ApplicationIssuingFeePage::acceptAndPay);
        Then("^I should be taken to the payment provider$", () -> Assert.assertThat(getURL().getHost(), is("sbsctest.e-paycapita.com")));
        }

    public static void triggerPermitIssuing(World world) throws ElementDidNotAppearWithinSpecifiedTimeException, InterruptedException, MalformedURLException, IllegalBrowserException {
        getDriver().manage().deleteAllCookies();

        get(URL.build(ApplicationType.INTERNAL, Properties.get("env", true)).toString());

        EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
        Config config = new Configuration(env.toString()).getConfig();

        world.APIJourneySteps.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        org.dvsa.testing.lib.pages.internal.NavigationBar.administratorButton();
        org.dvsa.testing.lib.pages.internal.NavigationBar.administratorList(AdminOption.PERMITS);

        String url = getURL().toString();
        // for each Annual ECMT stock
        // change to do while
        boolean wentToNext = false;
        AtomicInteger lastPage = new AtomicInteger(1);
        do {
            Permit.untilOnPage();
            sleep(500);
            List<Permit.Stock> stocks = Permit.stocks();
            IntStream.range(0, stocks.size()).forEach((nthPermit) -> {
                //int lastPage = Permit.currentPage();

                // Checks that type is ECMT and the validity period ends today or after today
                if (Permit.nthStockTypeIs(nthPermit, org.dvsa.testing.lib.enums.PermitType.ECMT_ANNUAL) &&
                        stocks.get(nthPermit).getValidFrom().isBefore(LocalDate.now()) &&
                        stocks.get(nthPermit).getValidTo().isAfter(LocalDate.now()) ||
                        stocks.get(nthPermit).getValidTo().isEqual(LocalDate.now())) {
                    Permit.nthStock(nthPermit + 1);

                    BaseAdminPermits.SideBar.select(BaseAdminPermits.SideBar.PermitsSidebarItem.Window);

                    // Close windows that end more than 30 minutes from now
                    IntStream.range(0, Window.numOfWindows()).forEach((nthWin) -> {

                        if (Window.nthWindowEndsAfter(nthWin, LocalDateTime.now().plusMinutes(30))) {
                            Window.selectNthWindow(nthWin + 1);
                            Window.edit();
                            Window.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);

                            //TODO: Remove edge case when the next time available falls on the next day
                            List<LocalTime> availableCloseTimes = Window.Model.getAllTimes(Window.Point.To);
                            LocalDateTime now = LocalDateTime.now();
                            LocalTime closestClosingTime = TimeUtils.closestTimeFrom(now.toLocalTime(), availableCloseTimes);
                            Window.Model.date(Window.Point.To, now.toLocalDate());
                            Window.Model.time(Window.Point.To, closestClosingTime);
                            Window.save();
                            Window.Model.untilModalIsGone(Duration.LONG, TimeUnit.SECONDS);

                            try {
                                TimeUnit.SECONDS.sleep(LocalTime.now().until(closestClosingTime, ChronoUnit.SECONDS));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Permit.SideBar.select(BaseAdminPermits.SideBar.PermitsSidebarItem.Scoring);

                            getDriver().navigate().refresh();

                            Scoring.run(Duration.CENTURY, TimeUnit.SECONDS);
                            Scoring.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);
                            Scoring.Model.continueButton();

                            Scoring.untilScoringStatusIs(Scoring.Status.ScoringSuccessful, Duration.CENTURY, TimeUnit.MINUTES);

                            Scoring.accept(Duration.LONG, TimeUnit.MINUTES);
                            Scoring.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);
                            Scoring.Model.continueButton();
                            Scoring.Model.untilModalIsGone(Duration.LONG, TimeUnit.SECONDS);

                            Scoring.untilScoringStatusIs(Scoring.Status.AcceptanceSuccessful, Duration.CENTURY, TimeUnit.MINUTES);
                        }

                    });

                    getDriver().get(url);


                    Permit.untilOnPage();
                    Permit.nthPage(lastPage.incrementAndGet());
                }
            });


            if (Permit.hasNextPage()) {
                Permit.next();
                wentToNext = true;
            } else {
                wentToNext = false;
            }
        } while(wentToNext);
    }
}