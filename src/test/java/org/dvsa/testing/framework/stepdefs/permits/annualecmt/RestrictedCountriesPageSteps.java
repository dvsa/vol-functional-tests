package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.newPages.permits.pages.RestrictedCountriesPage;
import org.dvsa.testing.lib.pages.external.permit.*;
import org.dvsa.testing.lib.pages.external.permit.enums.RestrictedCountry;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.BasePage.getURL;
import static org.hamcrest.CoreMatchers.containsString;

public class RestrictedCountriesPageSteps implements En {

    public RestrictedCountriesPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the restricted countries page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPage.checkNeedECMTPermits();
            CheckIfYouNeedECMTPermitsPage.saveAndContinue();
            CabotagePage.wontCarryCabotage(true);
            CertificatesRequiredPage.certificatesRequired(true);
        });
        Given("^I have selected some restricted countries$", () -> {
            RestrictedCountriesPage.countries(RestrictedCountry.random());
            CommonSteps.origin.put("origin", getURL());
        });
        Given("^I (do |don't )?plan on delivering to a restricted country$", (String deliverToRestrictedCountries) -> {
            boolean deliverToRestricted = deliverToRestrictedCountries.equals("do ");
            RestrictedCountriesPage.deliverToRestrictedCountry(deliverToRestricted);
        });
        Then("^the Advisory text on Annual ECMT countries with limited countries page is Shown Correctly$", () -> {
            RestrictedCountriesPage.hasAdvisoryText();
        });
        Then("^the page heading on Annual ECMT countries with limited countries page is Shown Correctly$", () -> {
            RestrictedCountriesPage.hasECMTPageHeading();
        });
        Then("^the application reference number is shown correctly$", () -> {
            String expectedLicenceNumber = operatorStore.getCurrentLicenceNumber().orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber = BasePermitPage.getReferenceFromPage();
            Assert.assertThat(actualReferenceNumber, containsString(expectedLicenceNumber));
        });

    }
}