package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.isPath;
import static org.junit.Assert.assertTrue;

public class CertificatesRequiredPageSteps implements En {

    public CertificatesRequiredPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the ECMT Removals certificates required page$", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
                    .licencePage(operatorStore, world);
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.RemovalsEligibility);
            EcmtInternationalRemovalJourney.getInstance()
                    .removalsEligibility(true)
                    .cabotagePage();
        });
        And ("^the application reference number is displayed$", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);
        });
        And ("^the advisory texts on certificates required page are displayed", () -> {
            assertTrue(CertificatesRequiredPage.isAdvisoryTextPresent());
        });
        And ("^the correct text is displayed next to the checkbox$", () -> {
            assertTrue(CertificatesRequiredPage.isCheckboxTextPresent());
        });
        And ("^I am on the ecmt removals permit start date page$", () -> {
            isPath("/application/\\d+/permit-start-date/");
        });

    }
}
