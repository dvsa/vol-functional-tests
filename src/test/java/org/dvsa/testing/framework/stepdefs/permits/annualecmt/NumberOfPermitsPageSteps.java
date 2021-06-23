package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.CheckIfYouNeedECMTPermitsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourneySteps;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.*;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.BasePage.isPath;
import static org.junit.Assert.assertTrue;

public class NumberOfPermitsPageSteps implements En {

    public NumberOfPermitsPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the number of permits page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPageJourneySteps.completePage();
            CabotagePage.confirmWontUndertakeCabotage();
            CertificatesRequiredPage.completePage();
            CountriesWithLimitedPermitsPage.noCountriesWithLimitedPermits();
        });
        And("^the page heading on the ECMT number of permits page is displayed correctly$", NumberOfPermitsPageJourneySteps::hasPageHeading);
        And("^the advisory texts are displayed correctly$", () -> {
            assertTrue(NumberOfPermitsPage.isAdvisoryTextPresent());
        });

        Given("^I have specified a number greater than the number of authorised vehicles$", NumberOfPermitsPage::exceedAuthorisedVehicle);
        Then("^I select the fee tab and pay the outstanding fees$", () -> {
            HomePage.selectTab(Tab.FEES);
            HomePage.FeesTab.hasOutstandingFees();
            HomePage.FeesTab.outstanbding(true);
            HomePage.FeesTab.pay();
            HomePage.FeesTab.payNowButton();
            EcmtApplicationJourney.getInstance()
                    .cardDetailsPage()
                    .cardHolderDetailsPage()
                    .confirmAndPay()
                    .passwordAuthorisation();
            FeePaymentSuccessful.untilFeePaid();

        });
        Then("^I am taken to the payment successful page$", () -> {
            isPath("//fees/receipt/");
            Assert.assertEquals(BasePage.getElementValueByText("//h1[@class='js-title']",SelectorType.XPATH),"Payment successful");
        });
    }
}
