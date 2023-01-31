package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import io.cucumber.java.en.And;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;

import static org.junit.jupiter.api.Assertions.*;


public class CancelApplicationPageSteps extends BasePage {
    @And("the cancel application page displays the correct text")
    public void theCancelApplicationPageDisplaysTheCorrectText() {
        CancellationPage.getPageHeading();
        assertTrue(CancellationPage.isAdvisoryTextPresent());
        assertEquals("I confirm that I would like to cancel my application.", CancellationPage.getConfirmCheckboxText());
    }
}