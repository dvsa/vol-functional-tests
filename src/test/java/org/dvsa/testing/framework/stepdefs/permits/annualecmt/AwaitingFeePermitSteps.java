package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.driver.Browser;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.IRHPPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ApplicationIssuingFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;

import static java.lang.Thread.sleep;
import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AwaitingFeePermitSteps extends BasePage implements En {

    private World world;

    public AwaitingFeePermitSteps(World world) {

        And("^I am viewing an application that's awaiting fees$", () -> {
            EcmtApplicationJourney.completeEcmtApplication(world);
            IRHPPageJourney.logInToInternalAndIRHPGrantApplication(world);
            sleep(3000);
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            HomePageJourney.selectPermitTab();
            Browser.navigate().navigate().refresh();
            untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
            HomePage.PermitsTab.selectFirstOngoingApplication();
        });
        When("^I cancel and return to dashboard from issuing fee page$", ApplicationIssuingFeePage::cancelAndReturnToDashboard);
        When("^I decline payment$", ApplicationIssuingFeePage::declinePermits);
        Then("^I should be on the decline awarded permits page$", () -> assertTrue(isPath("permits/application/\\d+/decline/")));
        When("^I accept and pay the issuing fee$", ApplicationIssuingFeePage::acceptAndPay);
        Then("^I should be taken to the payment provider$", () -> assertThat(getURL().getHost(), is("sbsctest.e-paycapita.com")));
    }
}