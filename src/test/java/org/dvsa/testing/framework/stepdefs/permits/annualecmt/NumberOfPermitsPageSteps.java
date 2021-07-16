package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.CheckIfYouNeedECMTPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.enums.Tab;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class NumberOfPermitsPageSteps extends BasePage implements En {

    public NumberOfPermitsPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the number of permits page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPageJourney.completePage();
            CabotagePage.confirmWontUndertakeCabotage();
            CabotagePage.saveAndContinue();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
        });
        And("^the page heading on the ECMT number of permits page is displayed correctly$", NumberOfPermitsPageJourney::hasPageHeading);
        And("^the advisory texts are displayed correctly$", () -> {
            assertTrue(NumberOfPermitsPage.isAdvisoryTextPresent());
        });

        Given("^I have specified a number greater than the number of authorised vehicles$", NumberOfPermitsPage::exceedAuthorisedVehicle);
        Then("^I select the fee tab and pay the outstanding fees$", () -> {
            HomePage.selectTab(Tab.FEES);
            assertTrue(HomePage.FeesTab.areOutstandingFeesPresent());
            HomePageJourney.payAllOutstandingFees();
            world.feeAndPaymentJourney.customerPaymentModule();
        });
        Then("^I am taken to the payment successful page$", () -> {
            isPath("//fees/receipt/");
            Assert.assertEquals(BasePage.getElementValueByText("//h1[@class='js-title']",SelectorType.XPATH),"Payment successful");
        });
    }
}
