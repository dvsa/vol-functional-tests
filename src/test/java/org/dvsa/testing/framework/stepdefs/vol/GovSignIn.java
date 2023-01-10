package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.junit.Assert;

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
}
