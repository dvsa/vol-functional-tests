package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;

import static org.junit.Assert.assertEquals;

public class CancelApplicationPageSteps extends BasePage implements En {

    public CancelApplicationPageSteps(World world) {
        And ("^the correct text is displayed next to the checkbox in ECMT Removal cancellation page", () -> {
            assertEquals("I confirm that I would like to cancel my application.", CancellationPage.getConfirmCheckboxText());
        });
        When("^the ECMT International Removal cancel application button is selected without checkbox ticked$", CancellationPage::clickCancelButton);
    }
}


