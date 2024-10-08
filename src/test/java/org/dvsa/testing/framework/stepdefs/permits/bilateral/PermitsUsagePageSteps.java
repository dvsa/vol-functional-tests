package org.dvsa.testing.framework.stepdefs.permits.bilateral;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.AnnualBilateralJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.PermitUsagePageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.PeriodSelectionPage;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitUsagePage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermitsUsagePageSteps {
    public PermitsUsagePageSteps(World world) {
    }

    @Then("I am on the Bilateral Permit usage page with correct information and content")
    public void iAmOnTheBilateralPermitUsagePage() {

        PermitUsagePage.untilOnPage();

        //Country name displayed on the Permits Usage page is the one clicked on the overview page
        assertEquals(PermitUsagePage.getCountry(), AnnualBilateralJourney.getCountry());

        // the page heading on bilateral Turkey permits usage  page is correct
        PermitUsagePageJourney.hasPageHeading();

        // I have only one option displayed by default i.e. Single Journey permits
        assertEquals("Only single journey permits are available for this country.", PermitUsagePage.getDefaultPeriodOption());
    }

    @When("I select continue button on the Bilateral permit usage page")
    public void iSelectContinueButtonOnTheBilateralPermit() {
        PeriodSelectionPage.saveAndContinue();
    }
}