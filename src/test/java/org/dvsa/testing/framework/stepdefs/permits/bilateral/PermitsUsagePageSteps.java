package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.PermitUsagePageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.external.enums.JourneyType;
import org.dvsa.testing.lib.newPages.external.pages.*;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class PermitsUsagePageSteps implements En {
    public PermitsUsagePageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on the Bilateral Permit usage page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitType.ANNUAL_BILATERAL, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnPage();
            OverviewPage.clickCountrySection(Country.Norway);
            EssentialInformationPage.untilOnPage();
            EssentialInformationPage.saveAndContinue();
            PeriodSelectionPage.untilOnPage();
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPage();
        });
        Then("^Country name displayed on the Permits Usage page is the one clicked on the overview page$", () -> {
            Assert.assertEquals(PermitUsagePage.getCountry(),operatorStore.getCountry());
        });

        Then("^the page heading on bilateral permits usage  page is correct$", PermitUsagePageJourney::hasPageHeading);
        Then("^I select a random Journey type and click continue$", () -> {
            PermitUsagePage.journeyType(JourneyType.random());
        });
        Then("^I am taken to the Bilateral Cabotage page$", CabotagePage::AnnualBilateralUntilOnPage);
        Then("^I get error message if there is more than one Journey types available to select and I click continue without making any selection$", PermitUsagePage::permitUsageError);
    }
}
