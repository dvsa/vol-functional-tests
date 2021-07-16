package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CertificatesRequiredPageSteps extends BasePage implements En {

    public CertificatesRequiredPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the ECMT Removals certificates required page$", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
                    .licencePage(operatorStore, world);
            OverviewPageJourney.clickOverviewSection(OverviewSection.RemovalsEligibility);
            EcmtInternationalRemovalJourney.getInstance()
                    .removalsEligibility(true)
                    .cabotagePage();
        });
        And ("^the application reference number is displayed$", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(BasePermitJourney.getReferenceNumber(), actualReference);
        });
        And ("^the advisory texts on certificates required page are displayed", () -> {
            assertEquals("If your permit application is successful, you are required to have the appropriate Certificate of Compliance and Certificate of Roadworthiness for each vehicle and trailer you intend to use.", CertificatesRequiredPage.getAdvisoryText());
        });
        And ("^the correct text is displayed next to the checkbox$", () -> {
            assertTrue(CertificatesRequiredPage.isCheckboxTextPresent());
        });
        And ("^I am on the ecmt removals permit start date page$", () -> {
            isPath("/application/\\d+/permit-start-date/");
        });

    }
}
