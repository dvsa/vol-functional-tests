package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.EssentialInformationPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PeriodSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PermitUsagePage;
import org.dvsa.testing.lib.pages.external.permit.enums.JourneyType;
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
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);
            PermitUsagePage.untilOnPermitUsagePage();
        });
        Then("^Country name displayed on the Permits Usage page is the one clicked on the overview page$", () -> {
            Assert.assertEquals(PermitUsagePage.getCountry(),operatorStore.getCountry());
        });

        Then("^the page heading on bilateral permits usage  page is correct$", () -> {
            String expectedPageHeading = "What do you need to use your permits for?";
            String actualPageHeading = PermitUsagePage.pageHeading().trim();
            Assert.assertEquals(expectedPageHeading, actualPageHeading);
        });
        Then("^I select a random Journey type and click continue$", () -> {
            PermitUsagePage.journeyType(JourneyType.random());
        });
        Then("^I am taken to the Bilateral Cabotage page$", CabotagePage::untilOnPage);
        Then("^I get error message if there is more than one Journey types available to select and I click continue without making any selection$", PermitUsagePage::PermitUsageError);
    }
}
