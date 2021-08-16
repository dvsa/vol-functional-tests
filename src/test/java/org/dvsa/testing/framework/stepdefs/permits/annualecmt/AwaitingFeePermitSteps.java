package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
<<<<<<< HEAD
import activesupport.IllegalBrowserException;
import activesupport.config.Configuration;
import activesupport.system.Properties;
import apiCalls.Utils.eupaBuilders.organisation.LicenceModel;
import apiCalls.eupaActions.OrganisationAPI;
import com.typesafe.config.Config;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.InternalBaseJourney;
import org.dvsa.testing.framework.Journeys.permits.internal.IRHPPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.exception.ElementDidNotAppearWithinSpecifiedTimeException;
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
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.IRHPPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ApplicationIssuingFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import org.junit.Assert;

import static java.lang.Thread.sleep;
import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.hamcrest.core.Is.is;

public class AwaitingFeePermitSteps extends BasePage implements En {

    private World world;

    public AwaitingFeePermitSteps(World world) {

        And("^I am viewing an application that's awaiting fees$", () -> {
            EcmtApplicationJourney.completeEcmtApplication(world);
            IRHPPageJourney.logInToInternalAndIRHPGrantApplication(world);
            sleep(3000);
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            getBrowser().navigate().refresh();
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
            HomePage.PermitsTab.selectFirstOngoingApplication();
        });
        When("^I cancel and return to dashboard from issuing fee page$", ApplicationIssuingFeePage::cancelAndReturnToDashboard);
        When("^I decline payment$", ApplicationIssuingFeePage::declinePermits);
        Then("^I should be on the decline awarded permits page$", () -> Assert.assertTrue(isPath("permits/application/\\d+/decline/")));
        When("^I accept and pay the issuing fee$", ApplicationIssuingFeePage::acceptAndPay);
        Then("^I should be taken to the payment provider$", () -> Assert.assertThat(getURL().getHost(), is("sbsctest.e-paycapita.com")));
    }
}