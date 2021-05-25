package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.permits.pages.CancellationPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CancelApplicationPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.PeriodSelectionPageOne;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.SelectYearPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class CancelApplicationPageSteps extends BasePage implements En {

    public CancelApplicationPageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on short term ECMT overview Page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            SelectYearPage.shortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodSelectionPageOne.ShortTermType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
        });
        Then("^I click cancel application link on the short term ECMT overview page$", OverviewPage::clickCancelApplication);
        Then("^I should be taken back to short Term Overview Page$", () -> {
            Assert.assertTrue(isPath("/permits/application/\\d+/"));
        });
        Then("^the short term ECMT CancelApplication page heading should be correct$", CancellationPage::untilOnPage);
        Then("^the short term  CancelApplication page displays the correct advisory text$", CancellationPage::assertAdvisoryTextOnCancelApplicationPage);
        Then("^I should get an error message on short term ECMT cancel application page$", CancelApplicationPage::errorMessage);
        And ("^I see the advisory text as per the AC$", CancellationPage::assertAdvisoryTextOnCancelApplicationPage);

    }
}
