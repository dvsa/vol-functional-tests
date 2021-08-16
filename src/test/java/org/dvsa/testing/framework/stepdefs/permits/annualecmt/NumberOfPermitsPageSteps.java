package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.pages.CheckIfYouNeedECMTPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.Tab;
import org.dvsa.testing.lib.newPages.external.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;
import org.dvsa.testing.lib.newPages.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.lib.newPages.BasePage;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.HomePageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.enums.Tab;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.BasePage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class NumberOfPermitsPageSteps extends BasePage implements En {

    public NumberOfPermitsPageSteps(World world) {
        And("^I am on the number of permits page$", () -> {
            EcmtApplicationJourney.completeUntilNumberOfPermitsPage(world);
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
