package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;


import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


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
            OverviewPage.clickCancelApplication();
        });
        And("^the licence number is displayed correctly$", () -> {
            String actualReferenceNumber = BasePermitPage.getReferenceFromPage();
            assertTrue(actualReferenceNumber.contains(world.applicationDetails.getLicenceNumber()));
        });
        And("^future sections beyond the current step are disabled$", () -> {
            assertFalse(OverviewPage.isActiveLinkPresent(OverviewSection.Cabotage));
        });
    }
}