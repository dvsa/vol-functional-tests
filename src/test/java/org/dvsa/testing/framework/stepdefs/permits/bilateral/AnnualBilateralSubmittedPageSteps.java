package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.junit.Assert;

public class AnnualBilateralSubmittedPageSteps extends BasePage implements En {
    public AnnualBilateralSubmittedPageSteps(World world) {
        Then("^I should not see the view receipt link$", () -> {
            Assert.assertFalse("'View  Receipt' link  should NOT be displayed but was", SubmittedPage.hasViewReceipt()
            );
        });
    }
}