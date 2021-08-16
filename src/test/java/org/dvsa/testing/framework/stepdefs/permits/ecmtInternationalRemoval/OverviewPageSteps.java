package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;


import Injectors.World;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class OverviewPageSteps implements En {
    public OverviewPageSteps(World world) {
        And("^I am on the ECMT International Removal overview page$", () -> {
            EcmtInternationalRemovalJourney.beginApplication(world);
        });
        And("^I click cancel application link on the International removal overview page$", () -> {
            BasePermitJourney.setFullReferenceNumber(BasePermitPage.getReferenceFromPage());
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