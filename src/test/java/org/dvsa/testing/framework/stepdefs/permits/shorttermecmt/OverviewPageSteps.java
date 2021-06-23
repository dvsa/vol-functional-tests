package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OverviewPageSteps implements En {

    public OverviewPageSteps(OperatorStore operatorStore, World world) {
        Then("^the advisory texts on shortterm overview page are displayed correctly$", () -> {
            String heading = OverviewPage.getSubheadingText();
            assertEquals("You must complete all of the sections to apply for a new permit.\nFurther guidance on permits is available on GOV.UK.", heading);
        });
        Then("^there is a guidance on permits link$", OverviewPage::isGuidanceOnPermitsLinkPresent);
        Then("^the default section status are displayed as expected$", () -> {

            OverviewPageJourneySteps.checkStatus(
                    OverviewSection.HowWillYouUseThePermits, PermitStatus.NOT_STARTED_YET);

            OverviewPageJourneySteps.checkStatus(
                    OverviewSection.Cabotage, PermitStatus.CANT_START_YET);

            OverviewPageJourneySteps.checkStatus(
                    OverviewSection.CertificatesRequired, PermitStatus.CANT_START_YET);

            OverviewPageJourneySteps.checkStatus(OverviewSection.NumberOfPermits, PermitStatus.CANT_START_YET);

            OverviewPageJourneySteps.checkStatus(OverviewSection.EuroEmissionStandards, PermitStatus.CANT_START_YET);

            OverviewPageJourneySteps.checkStatus(OverviewSection.CheckYourAnswers, PermitStatus.CANT_START_YET);

            OverviewPageJourneySteps.checkStatus(OverviewSection.Declaration, PermitStatus.CANT_START_YET);
        });
        And("^future sections on shortterm overview page beyond the current step are disabled$", () -> {
            assertTrue(OverviewPage.isActiveLinkPresent(OverviewSection.LicenceNumber));
        });
        When("^I select number of permits hyperlink from overview page$", () -> {
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.NumberOfPermits);
        });
    }
}
