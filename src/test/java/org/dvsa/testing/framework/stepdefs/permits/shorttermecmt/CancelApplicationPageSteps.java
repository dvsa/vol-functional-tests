package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

<<<<<<< HEAD
import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
=======
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CancelApplicationPageSteps extends BasePage implements En {

    public CancelApplicationPageSteps(World world) {
        And ("^the cancel application page displays the correct text$", () -> {
            CancellationPage.getPageHeading();
            assertTrue(CancellationPage.isAdvisoryTextPresent());
            assertEquals("I confirm that I would like to cancel my application.", CancellationPage.getConfirmCheckboxText());
        });

    }
}
