package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumberOfPermitsPageSteps {
    @Then("I should get the number of permits page error message")
    public void iShouldGetTheNumberOfPermitsPageErrorMessage() {
        assertTrue(NumberOfPermitsPage.isEnterNumberOfPermitsErrorTextPresent());
    }

    @Then("I should get the maximum number of permits exceeded page error message")
    public void IShouldGetTheMaximumNumberOfPermitsExceededPage() {
        assertTrue(NumberOfPermitsPage.isMaximumNumberOfPermitsExceededErrorTextPresent());
    }
    @Then("I should get the number of permits page error message on short term")
    public void iShouldGetTheNumberOfPermitsPageErrorMessageOnShortTerm() {
        assertTrue(NumberOfPermitsPage.isShortTermEnterNumberOfPermitsErrorTextPresent());
        assertTrue(NumberOfPermitsPage.isShortTermECMTEmissionErrorTextPresent());
    }

    @Then("I enter the valid number of short term permits required")
    public void iEnterTheValidNumberOfShortTermPermitsRequired() {
        NumberOfPermitsPage.enterEuro5OrEuro6permitsValue();
    }
}