package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import io.cucumber.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CancellationPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CancelApplicationPageSteps extends BasePage implements En {

    public CancelApplicationPageSteps(World world) {
        And ("^the cancel application page displays the correct text$", () -> {
            CancellationPage.getPageHeading();
            assertTrue(CancellationPage.isAdvisoryTextPresent());
            assertEquals("I confirm that I would like to cancel my application.", CancellationPage.getConfirmCheckboxText());
        });

    }
}
