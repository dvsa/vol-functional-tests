package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.CancelApplicationPage;
import org.junit.Assert;

public class CancelApplicationPageSteps implements En {

    public CancelApplicationPageSteps(OperatorStore operatorStore, World world) {

        And ("^the ECMT International Removal application reference number should be displayed above the heading$", () -> {
            String actualReference = CancelApplicationPage.reference();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);
        });
        Then("^I am on the ECMT Removals cancel application page$", CancelApplicationPage::untilOnPage);
        And ("^the ECMT international removal  CancelApplication heading should be correct$", CancelApplicationPage::untilOnPage);
        And ("^the ECMT International Removal CancelApplication page displays the correct advisory text$", CancelApplicationPage::hasAdvisoryText);
        And ("^the correct text is displayed next to the checkbox in ECMT Removal cancellation page", CancelApplicationPage::ecmtInternationalRemovalcancelConfirmationText);
        When("^the ECMT International Removal cancel application button is selected without checkbox ticked$", CancelApplicationPage::cancelApplication);
        When("^I select the Gov.UK hyperlink$", CancelApplicationPage::govUKLink);
    }
}


