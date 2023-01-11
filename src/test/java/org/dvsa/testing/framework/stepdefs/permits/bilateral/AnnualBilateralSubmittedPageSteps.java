package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class AnnualBilateralSubmittedPageSteps extends BasePage {
    public AnnualBilateralSubmittedPageSteps(World world) {
    }

    @Then("I should not see the view receipt link")
    public void iShouldNotSeeTheViewReceiptLink() {
        assertFalse(SubmittedPage.hasViewReceipt(),"'View  Receipt' link  should NOT be displayed but was");
    }
}