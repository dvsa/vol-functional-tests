package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.PermitTypePage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PeriodSelectionPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.PermitUsagePage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage.*;

public class PeriodSelectionPageSteps implements En {
    public PeriodSelectionPageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on the Bilateral Period Selection page$", () -> {
            clickToPermitTypePage(world);
            AnnualBilateralJourney.getInstance()
                    .permitType(PermitTypePage.PermitType.AnnualBilateral, operatorStore)
                    .licencePage(operatorStore, world);
            AnnualBilateralJourney.getInstance().norway(operatorStore);
            OverviewPage.untilOnOverviewPage();
            OverviewPage.clickNorway();
            untilOnPage();
            EssentialInformationPage.bilateralEssentialInfoContinueButton();
            untilOnPeriodSelectionPage();
        });
        Then("^Country name displayed on the page is the one clicked on the overview page$", () -> {
            Assert.assertEquals(PeriodSelectionPage.getCountry(),operatorStore.getCountry());
        });

        Then("^the page heading on bilateral period selection  page is correct$", () -> {
            String expectedPageHeading = "Validity period";
            String actualPageHeading = PeriodSelectionPage.pageHeading().trim();
            Assert.assertEquals(expectedPageHeading, actualPageHeading);
        });
        Then("^I select BilateralCabotagePermitsOnly period and click continue$", () -> {
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodSelectionPage.BilateralPeriodType.BilateralCabotagePermitsOnly,operatorStore);

        });
        Then("^I am taken to the Bilateral permits use page$", () -> {
            PermitUsagePage.untilOnPermitUsagePage();
        });
        Then("^I get error message if there is more than one period available to select and I click continue without making any selection$", () -> {
            PeriodSelectionPage.MultiplePeriodPresentError();
        });
    }
}

