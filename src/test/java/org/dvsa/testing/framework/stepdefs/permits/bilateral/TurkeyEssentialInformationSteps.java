package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Utils.common.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage;
import org.junit.Assert;

import static org.dvsa.testing.lib.pages.external.permit.bilateral.EssentialInformationPage.*;

public class TurkeyEssentialInformationSteps implements En {
    public TurkeyEssentialInformationSteps(OperatorStore operatorStore, World world) {

        Then("^I am navigated to Turkey essential information page with correct information and content$", () -> {
            // Make sure the page has loaded before any further checks
            untilOnPage();

            //checking the Country name displayed on the page is Turkey
            Assert.assertEquals(getCountry(),operatorStore.getCountry());

            //checking the Page heading on the Turkey essential information page is correct
            String expectedPageHeading = "Essential information";
            String actualPageHeading = pageHeading().trim();
            Assert.assertEquals(expectedPageHeading, actualPageHeading);

            //checking the page content on Bilateral essential information  page is correct
            turkeyPageContent();
        });
        When("^I select continue button on the Bilateral Turkey essential information page$", EssentialInformationPage::bilateralEssentialInfoContinueButton);
    }
}

