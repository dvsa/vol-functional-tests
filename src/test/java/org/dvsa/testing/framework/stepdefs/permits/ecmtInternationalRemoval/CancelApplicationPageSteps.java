package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

<<<<<<< HEAD
import io.cucumber.java8.En;;
=======
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import Injectors.World;
import cucumber.api.java8.En;
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


