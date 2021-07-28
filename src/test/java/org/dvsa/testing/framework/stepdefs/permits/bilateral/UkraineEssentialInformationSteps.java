package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EssentialInformationPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.newPages.external.pages.EssentialInformationPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class UkraineEssentialInformationSteps implements En {
    public UkraineEssentialInformationSteps(OperatorStore operatorStore, World world) {

        Then("^I am navigated to Ukraine essential information page with correct information and content$", () -> {
            // Make sure the page has loaded before any further checks
            EssentialInformationPage.untilOnPage();

            //checking the Country name displayed on the page is Turkey
            Assert.assertEquals(BasePermitPage.getCountry(),operatorStore.getCountry());

            //checking the Page heading on the Turkey essential information page is correct
            EssentialInformationPageJourney.hasPageHeading();

            //checking the page content on Bilateral essential information  page is correct
            assertTrue(EssentialInformationPage.isUkrainePageContentPresent());
        });
        When("^I select continue button on the Bilateral Ukraine essential information page$", () -> {
            EssentialInformationPage.saveAndContinue();
        });
    }
}

