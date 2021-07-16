package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.AnnualBilateralJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.PeriodSelectionPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.EssentialInformationPage;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.external.pages.PeriodSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.PermitUsagePage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class PeriodSelectionPageSteps extends BasePermitPage implements En {
    public PeriodSelectionPageSteps(OperatorStore operatorStore, World world) {
        Then("^I am on the Bilateral Period Selection page$", () -> {
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
        });
        Then("^Country name displayed on the page is the one clicked on the overview page$", () -> {
            Assert.assertEquals(PeriodSelectionPage.getCountry(),operatorStore.getCountry());
        });

        Then("^the page heading on bilateral period selection  page is correct$", () -> {
            PeriodSelectionPageJourney.hasPageHeading();
        });
        Then("^I select BilateralCabotagePermitsOnly period and click continue$", () -> {
            AnnualBilateralJourney.getInstance().bilateralPeriodType(PeriodType.BilateralCabotagePermitsOnly,operatorStore);

        });
        Then("^I am taken to the Bilateral permits use page$", () -> {
            PermitUsagePage.untilOnPage();
        });
        Then("^I get error message if there is more than one period available to select and I click continue without making any selection$", () -> {
            scrollAndClick("//input[@value = 'Save and continue']", SelectorType.XPATH);
            Assert.assertEquals(getText("//p[@class='error__text']", SelectorType.XPATH), "You must select one period to continue");
        });
    }
}

