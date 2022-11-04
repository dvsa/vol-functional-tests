package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;


import static org.junit.jupiter.api.Assertions.assertFalse;
public class AnnualBilateralSubmittedPageSteps extends BasePage implements En {
    public AnnualBilateralSubmittedPageSteps(World world) {
        Then("^I should not see the view receipt link$", () -> {
                    assertFalse(SubmittedPage.hasViewReceipt()
            );
        });
    }
}