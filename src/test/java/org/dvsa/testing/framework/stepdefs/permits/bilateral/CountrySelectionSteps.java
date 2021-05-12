package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.bilateral.CountrySelectionPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.RestrictedCountriesPage;
import org.hamcrest.text.MatchesPattern;
import org.junit.Assert;

public class CountrySelectionSteps implements En {
    public CountrySelectionSteps(OperatorStore operatorStore, World world) {
        Then("^bilateral country selection page licence reference number is correct$", () -> {
            String expectedLicenceNumber = operatorStore.getLatestLicence().get().getLicenceNumber();
            String actualReferenceNumber = RestrictedCountriesPage.reference();
            Assert.assertThat(actualReferenceNumber, MatchesPattern.matchesPattern(expectedLicenceNumber.concat(" / \\d+")));
        });
        Then("^the page heading on bilateral country selection  page is correct$", () -> {
            String expectedPageHeading = "Select the countries you are transporting goods to or transiting through";
            String actualPageHeading = CountrySelectionPage.pageHeading().trim();
            Assert.assertEquals(expectedPageHeading, actualPageHeading);
        });

        Then("^I am on the Bilaterals country selection page$", () -> {
            CountrySelectionPage.untilOnPage();
        });

    }
}

