package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.pages.EssentialInformationPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.pageObjects.external.pages.EssentialInformationPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class EssentialInformationSteps implements En {
    public EssentialInformationSteps(OperatorStore operatorStore, World world) {

        Then("^I am navigated to (.+) essential information page with correct information and content$", (String country) -> {
            // Make sure the page has loaded before any further checks
            EssentialInformationPage.untilOnPage();

            //checking the Country name displayed on the page is Turkey
            Assert.assertEquals(BasePermitPage.getCountry(), operatorStore.getCountry());

            //checking the Page heading on the essential information page is correct
            EssentialInformationPageJourney.hasPageHeading();

            if (country.equals("Turkey")) {
                //checking the page content on Bilateral essential information  page is correct
                assertTrue(EssentialInformationPage.isTurkeyPageContentPresent());
            } else if (country.equals("Ukraine")) {
                //checking the page content on Bilateral essential information  page is correct
                assertTrue(EssentialInformationPage.isUkrainePageContentPresent());
            }

        });
        When("^I select continue button on the Bilateral (.+) essential information page$", (String) -> {
            EssentialInformationPage.saveAndContinue();
        });
    }
}

