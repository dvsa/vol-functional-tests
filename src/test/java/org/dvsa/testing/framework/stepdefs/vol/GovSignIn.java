package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.OperatorType;
import apiCalls.enums.UserType;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.junit.Assert;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;

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
    }

    @And("I am taken back to VOL Review and Declarations page")
    public void iAmTakenBackToVOLReviewAndDeclarationsPage() {
        Assert.assertTrue(isTextPresent("Returning you to the ‘Vehicle Operator Licence’ service"));
        waitForTitleToBePresent("Review and declarations");
        Assert.assertTrue(isTextPresent("Review and declarations"));
    }

    @Then("the VOL {string} post signature page is displayed")
    public void theVOLAwaitingOperatorReviewPostSignaturePageIsDisplayed(String text) {
        waitForElementToBePresent("//*[@class='govuk-panel govuk-panel--confirmation']");
        Assert.assertTrue(isTextPresent(text));
    }

    @Then("the application should be digitally signed")
    public void theApplicationShouldBeDigitallySigned() {
        waitForTitleToBePresent("Review and declarations");
        Assert.assertTrue(isTextPresent("Declaration signed through GOV.UK One Login"));
        Assert.assertTrue(isTextPresent(String.format("Signed by Kenneth Decerqueira on %s", getCurrentDate("dd MMM yyyy"))));
    }
}
