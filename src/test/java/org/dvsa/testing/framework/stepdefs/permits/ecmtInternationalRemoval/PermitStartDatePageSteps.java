package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.ecmtInternationalRemoval.PermitStartDatePage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.isPath;

public class PermitStartDatePageSteps implements En {

    public PermitStartDatePageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the ECMT removals permit start page$", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.EcmtInternationalRemoval, operatorStore)
                    .licencePage(operatorStore, world);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewPage.Section.RemovalsEligibility, operatorStore)
                    .removalsEligibility(true)
                    .cabotagePage()
                    .certificatesRequiredPage();
        });
        And ("^the reference number is displayed correctly$", () -> {
            String actualReference = PermitStartDatePage.reference();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);
        });
        And ("^the page heading on permit start date page should be correct$", PermitStartDatePage::hasPageHeading);
        And ("^the advisory texts on permit start date page are displayed correctly$", PermitStartDatePage::hasAdvisoryText);
        Then  ("^the error message is displayed in the permit start date page$", PermitStartDatePage::errorText);
        And ("^I am taken to the number of permits page$", () -> {
            isPath("/permits/application/\\d+/number-of-permits/");
        });
        When ("^I enter the valid date$", PermitStartDatePage::permitDate);
        When ("^I dont enter all the fields$", PermitStartDatePage::leaveDateBlank);
        When ("^I enter invalid date$", PermitStartDatePage::inValidDate);
        And ("^I enter a date ahead of 60 days$", PermitStartDatePage::dayAhead);
        And ("^I should get a valid error message$", PermitStartDatePage::daysAheadErrorMessage);
    }
}
