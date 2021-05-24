package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.dvsa.testing.lib.newPages.permits.pages.CountrySelectionPage;
import org.hamcrest.text.MatchesPattern;
import org.junit.Assert;

public class CountrySelectionSteps implements En {
    public CountrySelectionSteps(OperatorStore operatorStore, World world) {
        Then("^bilateral country selection page licence reference number is correct$", () -> {
            String expectedLicenceNumber = operatorStore.getLatestLicence().get().getLicenceNumber();
            String actualReferenceNumber = BasePermitPage.getReferenceFromPage();
            Assert.assertThat(actualReferenceNumber, MatchesPattern.matchesPattern(expectedLicenceNumber.concat(" / \\d+")));
        });
        Then("^the page heading on bilateral country selection  page is correct$", CountrySelectionPage::hasPageHeading);

        Then("^I am on the Bilaterals country selection page$", CountrySelectionPage::untilOnPage);

    }
}

