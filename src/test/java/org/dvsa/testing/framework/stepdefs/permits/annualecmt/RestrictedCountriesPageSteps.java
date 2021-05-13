package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.pages.external.permit.CabotagePage;
import org.dvsa.testing.lib.pages.external.permit.CertificatesRequiredPage;
import org.dvsa.testing.lib.pages.external.permit.OverviewPage;
import org.dvsa.testing.lib.pages.external.permit.RestrictedCountriesPage;
import org.dvsa.testing.lib.pages.external.permit.ecmt.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.enums.PermitSection;
import org.dvsa.testing.lib.pages.external.permit.enums.RestrictedCountry;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.BasePage.getURL;
import static org.hamcrest.CoreMatchers.containsString;

public class RestrictedCountriesPageSteps implements En {

    public RestrictedCountriesPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the restricted countries page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPage.section(PermitSection.CheckIfYouNeedECMTPermits);
            CheckIfYouNeedECMTPermitsPage.needECMTPermits(true);
            CabotagePage.wontCarryCabotage(true);
            CertificatesRequiredPage.certificatesRequired(true);
        });
        Given("^I have selected some restricted countries$", () -> {
            RestrictedCountriesPage.countries(RestrictedCountry.random());
            world.put("origin", getURL());
        });
        Given("^I (do |don't )?plan on delivering to a restricted country$", (String deliverToRestrictedCountries) -> {
            boolean deliverToRestricted = deliverToRestrictedCountries.equals("do ");
            RestrictedCountriesPage.deliverToRestrictedCountry(deliverToRestricted);
        });
        And("^don't specify (?:which|anything)$", () -> {
            // Here for readability
        });
        Then("^I should see the validation error\\(s\\) message for restricted countries page$", () -> Assert.assertTrue(RestrictedCountriesPage.hasErrorMessagePresent()));
        Then("^the Advisory text on Annual ECMT countries with limited countries page is Shown Correctly$", () -> {
            RestrictedCountriesPage.hasAdvisoryText();
        });
        Then("^the page heading on Annual ECMT countries with limited countries page is Shown Correctly$", () -> {
            RestrictedCountriesPage.hasPageHeading();
        });
        Then("^the application reference number is shown correctly$", () -> {
            String expectedLicenceNumber = operatorStore.getCurrentLicenceNumber().orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber = RestrictedCountriesPage.reference();
            Assert.assertThat(actualReferenceNumber, containsString(expectedLicenceNumber));
        });

    }
}