package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.SubmittedPage;
import org.junit.Assert;

public class AnnualBilateralSubmittedPageSteps extends BasePage implements En {
    public AnnualBilateralSubmittedPageSteps(LicenceStore licenceStore, OperatorStore operatorStore, World world) {
        Then("^I should not see the view receipt link$", () -> {
            Assert.assertFalse("'View  Receipt' link  should NOT be displayed but was", SubmittedPage.hasViewReceipt()
            );
        });
    }
}