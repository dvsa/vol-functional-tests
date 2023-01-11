package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CancelApplicationPageSteps extends BasePage {

    @And("the correct text is displayed next to the checkbox in ECMT Removal cancellation page")
    public void theCorrectTextIsDisplayedNextToTheCheckbox() {
        assertEquals("I confirm that I would like to cancel my application.", CancellationPage.getConfirmCheckboxText());
    }

    @When("the ECMT International Removal cancel application button is selected without checkbox ticked")
    public void theECMTInternationalRemovalCancel() {
        CancellationPage.clickCancelButton();
    }
}