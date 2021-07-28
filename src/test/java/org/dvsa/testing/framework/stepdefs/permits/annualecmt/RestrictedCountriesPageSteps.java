package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.CheckIfYouNeedECMTPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.RestrictedCountriesPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.enums.RestrictedCountry;
import org.dvsa.testing.lib.newPages.external.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.external.pages.RestrictedCountriesPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

public class RestrictedCountriesPageSteps extends BasePage implements En {

    public RestrictedCountriesPageSteps(World world, OperatorStore operatorStore) {
        And("^I am on the restricted countries page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            OverviewPageJourney.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPageJourney.completePage();
            CabotagePage.confirmWontUndertakeCabotage();
            CabotagePage.saveAndContinue();
            CertificatesRequiredPage.completePage();
        });
        Given("^I have selected some restricted countries$", () -> {
            RestrictedCountriesPage.countries(RestrictedCountry.random());
        });
        Given("^I (do |don't )?plan on delivering to a restricted country$", (String deliverToRestrictedCountries) -> {
            boolean deliverToRestricted = deliverToRestrictedCountries.equals("do ");
            RestrictedCountriesPage.deliverToRestrictedCountry(deliverToRestricted);
        });
        Then("^the Advisory text on Annual ECMT countries with limited countries page is Shown Correctly$", () -> {
                String advisoryText = RestrictedCountriesPage.getAdvisoryText();
                assertEquals("There is a very small number of permits available for these countries.\n" +
                        "We cannot guarantee if you receive a permit that it will allow you to travel to these countries.\n" +
                        "Annual ECMT Euro 5 permits are not valid for journeys to or through Austria.", advisoryText);
        });
        Then("^the page heading on Annual ECMT countries with limited countries page is Shown Correctly$", RestrictedCountriesPageJourney::hasPageHeading);
        Then("^the application reference number is shown correctly$", () -> {
            String actualReferenceNumber = BasePermitPage.getReferenceFromPage();
            Assert.assertThat(actualReferenceNumber, containsString(world.applicationDetails.getLicenceNumber()));
        });

    }
}