package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;

public class OverviewPageSteps implements En {

    public OverviewPageSteps(OperatorStore operatorStore, World world) {
        Then("^the advisory texts on shortterm overview page are displayed correctly$", OverviewPage::hasOverviewPageText);
        Then("^the page heading on short term Ecmt is displayed correctly$", OverviewPage::hasPageHeading);
        Then("^there is a guidance on permits link$", () -> {
          OverviewPage.hasGuidanceOnPermitsLink();
        });
        Then("^the default section status are displayed as expected$", () -> {

            OverviewPage.checkStatus(
                    OverviewSection.HowWillYouUseThePermits, PermitStatus.NOT_STARTED_YET);

            OverviewPage.checkStatus(
                    OverviewSection.Cabotage, PermitStatus.CANT_START_YET);

            OverviewPage.checkStatus(
                    OverviewSection.CertificatesRequired, PermitStatus.CANT_START_YET);

            OverviewPage.checkStatus(
                    OverviewSection.NumberOfPermits, PermitStatus.CANT_START_YET);

            OverviewPage.checkStatus(
                    OverviewSection.EuroEmissionStandards, PermitStatus.CANT_START_YET);

            OverviewPage.checkStatus(
                    OverviewSection.CheckYourAnswers, PermitStatus.CANT_START_YET);

            OverviewPage.checkStatus(
                    OverviewSection.Declaration, PermitStatus.CANT_START_YET);
        });
        And("^future sections on shortterm overview page beyond the current step are disabled$", () -> {
            OverviewPage.hasActiveLink(OverviewSection.LicenceNumber);
        });
        When("^I select number of permits hyperlink from overview page$", () -> {
            OverviewPage.clickOverviewSection(OverviewSection.NumberOfPermits);
        });
    }
}
