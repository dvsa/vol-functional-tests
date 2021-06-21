package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.pages.external.permit.BaseOverviewPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitUsage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.*;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class CabotagePageSteps implements En {
    public CabotagePageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the shortterm cabotage page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitTypePage.PermitType.ShortTermECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            OverviewPage.select(OverviewPage.Section.HowwillyouusethePermits);
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
            boolean cabotage =  BaseOverviewPage.checkStatus(PermitSection.Cabotage.toString(),PermitStatus.COMPLETED);
            boolean permitUsage = OverviewPage.checkStatus(OverviewPage.Section.HowwillyouusethePermits.toString(),PermitStatus.COMPLETED);
            Assert.assertTrue(cabotage);
        });

    }
}
