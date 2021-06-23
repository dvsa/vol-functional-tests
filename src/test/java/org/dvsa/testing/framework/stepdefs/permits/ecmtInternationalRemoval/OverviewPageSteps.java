package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;


import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickCancelApplication;


public class OverviewPageSteps implements En {
    public OverviewPageSteps(OperatorStore operatorStore, World world) {
        And("^I am on the ECMT International Removal overview page$", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
            .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
            .licencePage(operatorStore, world);
        });
        And("^I click cancel application link on the International removal overview page$", () -> {
            operatorStore.getLatestLicence().get().setReferenceNumber(BasePermitPage.getReferenceFromPage());
            clickCancelApplication();
        });
        And("^the application number is displayed correctly$", () -> {
            String expectedLicenceNumber = operatorStore.getCurrentLicenceNumber()
                    .orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber = BasePermitPage.getReferenceFromPage();
            Assert.assertTrue(actualReferenceNumber.contains(expectedLicenceNumber));
        });
        And("^future sections beyond the current step are disabled$", () -> {
            OverviewPage.hasActiveLink(OverviewSection.LicenceNumber);
        });

    }
}