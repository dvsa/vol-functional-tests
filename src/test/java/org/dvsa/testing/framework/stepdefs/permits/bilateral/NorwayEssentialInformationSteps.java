package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EssentialInformationPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.enums.Country;
import org.dvsa.testing.lib.newPages.external.pages.EssentialInformationPage;
import org.dvsa.testing.lib.newPages.external.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.external.pages.PeriodSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class NorwayEssentialInformationSteps implements En {
    public NorwayEssentialInformationSteps(OperatorStore operatorStore, World world) {
        And("^I click on Norway country link on the Application overview page$", () -> {
            OverviewPage.clickCountrySection(Country.Norway);
        });

        When("^I am on the Norway essential information page$", EssentialInformationPage::untilOnPage);
        Then("^Country name displayed on the Bilateral permit essential information page is the one clicked on the overview page$", () -> {
            Assert.assertEquals(BasePermitPage.getCountry(),operatorStore.getCountry());
        });
        And("^the page heading on Bilateral essential information  page is correct$", EssentialInformationPageJourney::hasPageHeading);
        And("^the page content on Bilateral essential information  page is correct$", () -> {
            assertTrue(EssentialInformationPage.isPageContentPresent());
        });
        And("^the GOV.UK link on Bilateral essential information  page is correct$", EssentialInformationPage::hasInternationalAuthorisationGovGuidanceLink);
        And("^I am navigated to Bilaterals period selection page$", PeriodSelectionPage::untilOnPage);
        And("^I select continue button on the Bilateral essential information page$", EssentialInformationPage::saveAndContinue);
    }
}

