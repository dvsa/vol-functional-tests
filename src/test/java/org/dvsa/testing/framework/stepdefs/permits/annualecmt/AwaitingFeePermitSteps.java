package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.config.Configuration;
import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import com.typesafe.config.Config;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.InternalBaseJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.IRHPPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.external.pages.ApplicationIssuingFeePage;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;
import org.dvsa.testing.lib.newPages.internal.admin.permits.Permit;
import org.dvsa.testing.lib.newPages.internal.admin.permits.Scoring;
import org.dvsa.testing.lib.newPages.internal.admin.permits.SideBar;
import org.dvsa.testing.lib.newPages.internal.admin.permits.Window;
import org.dvsa.testing.lib.newPages.internal.admin.permits.enums.DateField;
import org.dvsa.testing.lib.newPages.internal.admin.permits.enums.PermitsSidebarItem;
import org.dvsa.testing.lib.newPages.internal.admin.permits.enums.ScoringStatus;
import org.dvsa.testing.lib.newPages.BasePage;
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
import static org.hamcrest.core.Is.is;

public class AwaitingFeePermitSteps extends BasePage implements En {

    private World world;

    public AwaitingFeePermitSteps(OperatorStore operatorStore, World world) {

        And("^I am viewing an application that's awaiting fees$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.beginPermitApplication();
            ECMTPermitApplicationSteps.completeEcmtApplication(operatorStore, world);
            operatorStore.setCurrentLicenceNumber(world.applicationDetails.getLicenceNumber());
            IRHPPageJourney.logInToInternalAndIRHPGrantApplication(world);
            sleep(3000);
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            getDriver().navigate().refresh();
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
            HomePage.PermitsTab.selectFirstOngoingApplication();
        });
        When("^I cancel and return to dashboard from issuing fee page$", ApplicationIssuingFeePage::cancelAndReturnToDashboard);
        When("^I decline payment$", ApplicationIssuingFeePage::declinePermits);
        Then("^I should be on the decline awarded permits page$", () -> Assert.assertTrue(isPath("permits/application/\\d+/decline/")));
        When("^I accept and pay the issuing fee$", ApplicationIssuingFeePage::acceptAndPay);
        Then("^I should be taken to the payment provider$", () -> Assert.assertThat(getURL().getHost(), is("sbsctest.e-paycapita.com")));
        }

    public static void triggerPermitIssuing(World world) throws InterruptedException {
        getDriver().manage().deleteAllCookies();

        get(URL.build(ApplicationType.INTERNAL, Properties.get("env", true)).toString());

        EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
        Config config = new Configuration(env.toString()).getConfig();

        world.APIJourney.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        InternalBaseJourney.navigateToAdminPermitsPage();

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
                if (Permit.nthStockTypeIs(nthPermit, PermitType.ECMT_ANNUAL) &&
                        stocks.get(nthPermit).getValidFrom().isBefore(LocalDate.now()) &&
                        stocks.get(nthPermit).getValidTo().isAfter(LocalDate.now()) ||
                        stocks.get(nthPermit).getValidTo().isEqual(LocalDate.now())) {
                    Permit.nthStock(nthPermit + 1);

                    SideBar.select(PermitsSidebarItem.Window);

                    // Close windows that end more than 30 minutes from now
                    IntStream.range(0, Window.numOfWindows()).forEach((nthWin) -> {

                        if (Window.nthWindowEndsAfter(nthWin, LocalDateTime.now().plusMinutes(30))) {
                            Window.selectNthWindow(nthWin + 1);
                            Window.edit();
                            Window.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);

                            LocalDateTime now = LocalDateTime.now();
                            LocalTime closestClosingTime = LocalTime.now().plusMinutes(1);
                            Window.Model.date(DateField.EndDate, now.toLocalDate());
                            Window.Model.time(DateField.EndDate, closestClosingTime);
                            Window.save();
                            Window.Model.untilModalIsGone();

                            try {
                                TimeUnit.SECONDS.sleep(LocalTime.now().until(closestClosingTime, ChronoUnit.SECONDS));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            SideBar.select(PermitsSidebarItem.Scoring);

                            getDriver().navigate().refresh();

                            Scoring.run(Duration.CENTURY, TimeUnit.SECONDS);
                            Scoring.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);
                            Scoring.Model.continueButton();

                            Scoring.untilScoringStatusIs(ScoringStatus.ScoringSuccessful, Duration.CENTURY, TimeUnit.MINUTES);

                            Scoring.accept(Duration.LONG, TimeUnit.MINUTES);
                            Scoring.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);
                            Scoring.Model.continueButton();
                            Scoring.Model.untilModalIsGone();

                            Scoring.untilScoringStatusIs(ScoringStatus.AcceptanceSuccessful, Duration.CENTURY, TimeUnit.MINUTES);
                        }

                    });

                    get(url);
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