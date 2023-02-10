package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.IRHPPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ApplicationIssuingFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;

import static java.lang.Thread.sleep;
import static org.dvsa.testing.framework.stepdefs.permits.annualecmt.ValidPermitsPageSteps.untilAnyPermitStatusMatch;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AwaitingFeePermitSteps extends BasePage {
    private final World world;
    public AwaitingFeePermitSteps(World world) {
        this.world = world;
    }
    @And("I am viewing an application that's awaiting fees")
    public void iAMViewingAnApplicationThatIsAwaitingFees() throws InterruptedException, HttpException {
        EcmtApplicationJourney.completeEcmtApplication(world);
        IRHPPageJourney.logInToInternalAndIRHPGrantApplication(world);
        sleep(3000);
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        HomePageJourney.selectPermitTab();
        getDriver().navigate().refresh();
        untilAnyPermitStatusMatch(PermitStatus.AWAITING_FEE);
        HomePage.PermitsTab.selectFirstOngoingApplication();
    }
    @When("I cancel and return to dashboard from issuing fee page")
    public void iCancelAndReturnToTheDashboard() {
        ApplicationIssuingFeePage.cancelAndReturnToDashboard();
    }
    @When("I decline payment")
    public void iDeclinePayment() {
        ApplicationIssuingFeePage.declinePermits();
    }
    @Then("I should be on the decline awarded permits page")
    public void iShouldBeOnTheDeclineAwardedPermitsPage() {
        assertTrue(isPath("permits/application/\\d+/decline/"));
    }
    @When("I accept and pay the issuing fee")
    public void iAcceptAndPayTheIssuingFee() {
        ApplicationIssuingFeePage.acceptAndPay();
    }

    @Then("I should be taken to the payment provider")
    public void iShouldBeTakenToThePaymentProvider() {
        assertThat(getURL().getHost(), is("sbsctest.e-paycapita.com"));
    }
}