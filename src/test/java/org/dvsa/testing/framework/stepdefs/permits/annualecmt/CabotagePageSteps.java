package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.pages.external.permit.CabotagePage;
import org.dvsa.testing.lib.pages.external.permit.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.ecmt.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.BasePage.getURL;

public class CabotagePageSteps implements En {

    public CabotagePageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the cabotage Page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPage.section(PermitSection.CheckIfYouNeedECMTPermits);
            CheckIfYouNeedECMTPermitsPage.needECMTPermits(true);

        });
        When("^I declare not to undertake cabotage$", () -> {
            CabotagePage.checkboxSelection();
        });
        Given("^I have not declared not to undertake cabotage$", () -> {
            world.put("origin", getURL());
        });
        Then("^I should see the validation error message for the cabotage page$", () -> Assert.assertTrue(CabotagePage.hasErrorMessagePresent()));
    }

}
