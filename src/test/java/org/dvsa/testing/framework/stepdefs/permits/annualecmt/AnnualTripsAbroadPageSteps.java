package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import cucumber.api.java8.StepdefBody;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.ecmt.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.shorttermecmt.EuroEmissioStandardsPage;
import org.junit.Assert;

public class AnnualTripsAbroadPageSteps implements En {

    public AnnualTripsAbroadPageSteps(World world, OperatorStore store) {
        And("^I am on the annual trips abroad page$", () -> {
            LicenceStore licenceStore = store.getCurrentLicence().orElseGet(LicenceStore::new);
            store.withLicences(licenceStore);
            OverviewPage.section(PermitSection.CheckIfYouNeedECMTPermits);
            CheckIfYouNeedECMTPermitsPage.needECMTPermits(true);
            CabotagePage.wontCarryCabotage(true);
            CertificatesRequiredPage.certificatesRequired(true);
            CountriesWithLimitedPermitsPage.noCountrieswithLimitedPermits();
            NumberOfPermitsPage.permitsValue();
            BasePermitPage.saveAndContinue();
            EuroEmissioStandardsPage.Emissionsconfirmation();
            BasePermitPage.saveAndContinue();

        });
        Given("^I specify a valid amount of annual trips$", NumberOfTripsPage::numberOfTripsValue);
        Given("^I specify an invalid input$", NumberOfTripsPage::inValidValue);

        Given("^I don't specify an amount for annual trips$", () -> {
            // Here for readability
        });
        Given("^I specify an invalid ([\\w\\-]+) of annual trips$", (StepdefBody.A1<String>) NumberOfTripsPage::quantity);
        Then("^I should see text informing me not to include NI journeys$", () -> {
            Assert.assertTrue(
                    "The text informing users not to include NI journeys was not found",
                    NumberOfTripsPage.hasInfoForNIUsers()
            );
        });
        And("^I don't specify a value for annual trips$", () -> {
            // Here for readability
        });
        Then("^I am informed that I may be asked to verify my answers on number of trips page$", () -> {
           //for readability
           // Assert.assertTrue("Unable to find intensity of use message on current page", NumberOfTripsPage.hasIntensityMessage())
        });
        Then("^I should see the validation error message for the number of trips page$", () -> Assert.assertTrue(NumberOfTripsPage.hasErrorMessagePresent()));
    }

}
