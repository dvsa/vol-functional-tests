package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage;
import org.dvsa.testing.lib.pages.external.permit.bilateral.OverviewPage;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage.*;

public class NorwayEssentialInformationSteps implements En {
    public NorwayEssentialInformationSteps(OperatorStore operatorStore, World world) {
        And("^I click on Norway country link on the Application overview page$", () -> {
            OverviewPage.clickNorway();
        });

        When("^I am on the Norway essential information page$", () -> {
            untilOnPage();
        });
        Then("^Country name displayed on the Bilateral permit essential information page is the one clicked on the overview page$", () -> {
            Assert.assertEquals(getCountry(),operatorStore.getCountry());
        });
        And("^the page heading on Bilateral essential information  page is correct$", () -> {
            String expectedPageHeading = "Essential information";
            String actualPageHeading = pageHeading().trim();
            Assert.assertEquals(expectedPageHeading, actualPageHeading);
        });
        And("^the page content on Bilateral essential information  page is correct$", () -> {
           pageContent();
        });
        And("^the GOV.UK link on Bilateral essential information  page is correct$", () -> {
            link();
        });
        And("^I am navigated to Bilaterals period selection page$", () -> {
            untilOnPeriodSelectionPage();
        });
        And("^I select continue button on the Bilateral essential information page$", () -> {
            EssentialInformationPage.bilateralEssentialInfoContinueButton();

        });
    }
}

