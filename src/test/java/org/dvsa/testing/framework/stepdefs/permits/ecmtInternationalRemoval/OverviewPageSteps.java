package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;


import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.OverviewPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.cancel;


public class OverviewPageSteps implements En {
    public OverviewPageSteps(OperatorStore operatorStore, World world) {
        And("^I am on the ECMT International Removal overview page$", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
            .permitType(PermitTypePage.PermitType.EcmtInternationalRemoval, operatorStore)
            .licencePage(operatorStore, world);
        });
        And("^I click cancel application link on the International removal overview page$", () -> {
            operatorStore.getLatestLicence().get().setReferenceNumber(org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.reference());
            cancel();
        });
        Then("^I should be on the ECMT International Overview Page$", OverviewPage::untilOnPage);
        And("^the application number is displayed correctly$", () -> {
            String expectedLicenceNumber = operatorStore.getCurrentLicenceNumber()
                    .orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber = org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.reference();
            Assert.assertTrue(actualReferenceNumber.contains(expectedLicenceNumber));
        });
        And("^the page heading on ECMT International Removal is correct$", () -> {
            org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.pageHeading();
        });
        And("^future sections beyond the current step are disabled$", () -> {
            org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.hasActiveLink(org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage.Section.LicenceNumber);
        });

    }
}