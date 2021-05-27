package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitUsage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CabotagePage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.PermitUsagePage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.SelectYearPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class CabotagePageSteps implements En {
    public CabotagePageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the shortterm cabotage page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
        });
        Then("^the page heading on the cabotage page is displayed correctly$", CabotagePage::hasPageHeading);
        Then("^the short term ecmt cabotage page has application reference number$",  () -> {
            String message = "Expected there to be a reference number displayed in the correct format but it wasn't";
            Assert.assertTrue(message,CabotagePage.hasReference());
        });
        Then("^I should get the cabotage page error message$", CabotagePage::hasErrorMessage);
        Then("^the shortterm ecmt cabotage page has advisory messages$", CabotagePage::hasAdvisoryText);

        Then("^I confirm not undertaking cabotage journey$", CabotagePage::cabotageConfirmation);
        Then("^the user is navigated to the overview page with the status as completed$", () -> {
            OverviewPage.checkStatus(OverviewSection.Cabotage,PermitStatus.COMPLETED);
            OverviewPage.checkStatus(OverviewSection.HowWillYouUseThePermits ,PermitStatus.COMPLETED);
        });
    }
}
