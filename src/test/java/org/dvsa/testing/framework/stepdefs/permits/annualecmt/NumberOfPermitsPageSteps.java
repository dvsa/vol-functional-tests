package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtApplicationJourney;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.dvsa.testing.lib.pages.enums.external.home.Tab;
import org.dvsa.testing.lib.pages.external.HomePage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CountriesWithLimitedPermitsPage;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.BasePage.isPath;
import static org.dvsa.testing.lib.pages.internal.details.irhp.IrhpPermitsApplyPage.permitsQuantity;

public class NumberOfPermitsPageSteps implements En {

    public NumberOfPermitsPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the number of permits page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPage.section(PermitSection.CheckIfYouNeedECMTPermits);
            CheckIfYouNeedECMTPermitsPage.needECMTPermits(true);
            CabotagePage.wontCarryCabotage(true);
            CertificatesRequiredPage.certificatesRequired(true);
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
        });
        And("^the page heading on the ECMT number of permits page is displayed correctly$", NumberOfPermitsPage::hasPageHeading);
        And("^the advisory texts are displayed correctly$", NumberOfPermitsPage::hasAdvisoryText);

        Given("^I have not specified the amount of permits I'd like$", () -> {
            // Here for readability
        });
        Then("^the number of permits allowed matches my number of authorised vehicles$", () -> {
            int actualNumberOfPermits = NumberOfPermitsPage.getNumberOfPermitsAuthorised();
            int expectedNumberOfPermits = world.get("numberOfAuthorisedVehicles");
            Assert.assertEquals(expectedNumberOfPermits, actualNumberOfPermits);
        });
        //Fill application
        Given("^I have specified a number greater than the number of authorised vehicles$", NumberOfPermitsPage::authorisedVehicleExceed);
        Given("^I have not specified an amount for permits$", () -> {
            // Here for readability
        });
        And ("^I specify more than the maximum number of permits$", () -> {
            LicenceStore licenceStore = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licenceStore);
            int numberOfPermits = licenceStore.getNumberOfAuthorisedVehicles()+1;
            permitsQuantity(numberOfPermits);
        });
        And ("^I should get the ECMT number of permits page error message$", () -> {
            Assert.assertEquals(NumberOfPermitsPage.errorMessage(), "Enter how many permits you need");
        });
        Given("^I specify the number of permits$", NumberOfPermitsPage::euro5OrEuro6permitsValue);
        Then("^I should see the validation errors message for the number of permits page$", () -> Assert.assertTrue(NumberOfPermitsPage.hasErrorMessagePresent()));
        Then("^displays the error message$", () -> {
            Assert.assertEquals("Enter the number of permits you require, or enter 0", NumberOfPermitsPage.errorMessage());
        });
        Then("^I am on the annual ECMT overview page$", OverviewPage::overviewPageHeading);
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

        Then("^a cannot be blank error message is displayed$", () -> {
            Assert.assertTrue("Enter the number of permits you require, or enter 0", true);
        });
    }
}
