package org.dvsa.testing.framework.stepdefs.vol;

import apiCalls.enums.OperatorType;
import apiCalls.enums.UserType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GovSignIn extends BasePage {
    private final World world;

    public GovSignIn(World world) {this.world = world;}

    @Given("I can navigate to gov sign in")
    public void iCanNavigateToGovSignIn() {
        world.govSignInJourney.navigateToGovUkSignIn();
    }

    @And("I sign in to gov sign in to complete the process")
    public void iSignInToGovSignInToCompleteTheProcess() {
        world.govSignInJourney.signInGovAccount();
    }

    @Then("i complete the payment process")
    public void iCompleteThePaymentProcess() {
        clickById("submitAndPay");
        clickById("form-actions[pay]");
        world.feeAndPaymentJourney.customerPaymentModule();
    }

    @Then("I register a gov sign in account to complete the process")
    public void iRegisterAGovSignInAccountToCompleteTheProcess() {
        world.govSignInJourney.registerGovAccount();
        assertTrue(isTextPresent("You’ve successfully proved your identity. You can now continue to the service you want to use."));
    }

    @And("I am taken back to VOL Review and Declarations page")
    public void iAmTakenBackToVOLReviewAndDeclarationsPage() {
        assertTrue(isTextPresent("Returning you to the ‘Vehicle Operator Licence’ service"));
        waitForTitleToBePresent("Review and declarations");
        assertTrue(isTextPresent("Review and declarations"));
    }

    @Then("the VOL {string} post signature page is displayed")
    public void theVOLAwaitingOperatorReviewPostSignaturePageIsDisplayed(String text) {
        waitForElementToBePresent("//*[@class='govuk-panel govuk-panel--confirmation']");
        assertTrue(isTextPresent(text));
    }

    @Then("the application should be digitally signed")
    public void theApplicationShouldBeDigitallySigned() {
        waitForTitleToBePresent("Review and declarations");
        assertTrue(isTextPresent("Declaration signed through GOV.UK One Login"));
        assertTrue(isTextPresent(String.format("Signed by Kenneth Decerqueira on %s", getCurrentDate("dd MMM yyyy"))));
    }
}