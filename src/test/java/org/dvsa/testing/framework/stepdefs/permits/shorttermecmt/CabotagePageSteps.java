package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.PeriodType;
import org.dvsa.testing.framework.pageObjects.enums.PermitUsage;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitUsagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;

public class CabotagePageSteps implements En {
    public CabotagePageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the shortterm cabotage page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            OverviewPageJourney.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
        });
        Then("^the page heading on the cabotage page is displayed correctly$", () -> {
            String heading = CabotagePage.getPageHeading();
            assertEquals("ECMT permits do not allow you to carry out cabotage", heading);
        });
        Then("^the short term ecmt cabotage page has application reference number$",  () -> {
            String message = "Expected there to be a reference number displayed in the correct format but it wasn't";
            Assert.assertTrue(message, CabotagePage.hasReference());
        });
        Then("^I should get the cabotage page error message$", () -> {
            String errorText = CabotagePage.getErrorText();
            Assert.assertEquals("Tick to confirm your vehicle will not undertake cabotage journeys", errorText);
        });
        Then("^I should get the cabotage page error message for Annual ECMTs$", () -> {
            String errorText = CabotagePage.getErrorText();
            Assert.assertEquals("Tick to confirm your vehicle will not undertake cabotage journeys.", errorText);
        });
        Then("^the shortterm ecmt cabotage page has advisory messages$", () -> {
            String advisoryText = CabotagePage.getAdvisoryText();
            assertEquals("Cabotage is the haulage and moving of goods for hire or reward between two points in " +
                    "the same country, carried out by a vehicle that is not registered in that country.", advisoryText);
        });

        Then("^I confirm not undertaking cabotage journey$", CabotagePage::confirmWontUndertakeCabotage);
        Then("^the user is navigated to the overview page with the status as completed$", () -> {
            OverviewPageJourney.checkStatus(OverviewSection.Cabotage,PermitStatus.COMPLETED);
            OverviewPageJourney.checkStatus(OverviewSection.HowWillYouUseThePermits ,PermitStatus.COMPLETED);
        });
    }
}
