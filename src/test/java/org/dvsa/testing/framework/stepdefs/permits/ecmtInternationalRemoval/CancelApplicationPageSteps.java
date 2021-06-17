package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.permits.pages.CancellationPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CancelApplicationPageSteps extends BasePage implements En {

    public CancelApplicationPageSteps(OperatorStore operatorStore, World world) {

        And ("^the ECMT International Removal application reference number should be displayed above the heading$", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);
        });
        And ("^the correct text is displayed next to the checkbox in ECMT Removal cancellation page", () -> {
            assertEquals("I confirm that I would like to cancel my application.", CancellationPage.getConfirmCheckboxText());
        });
        When("^the ECMT International Removal cancel application button is selected without checkbox ticked$", CancellationPage::clickCancelButton);
        When("^I select the Gov.UK hyperlink$", () -> {
            assertTrue(isElementNotPresent("//a[@href='https://www.gov.uk/guidance/international-authorisations-and-permits-for-road-haulage']", SelectorType.XPATH));
        });
    }
}


