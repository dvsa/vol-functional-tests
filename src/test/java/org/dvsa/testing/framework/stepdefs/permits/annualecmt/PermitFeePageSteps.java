package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermitFeePageSteps extends BasePermitPage {
    World world;

    public PermitFeePageSteps(World world) {
        this.world = world;
    }

    @And("I select the submit and pay link from overview page")
    public void iSelectTheSubmitAndPayLinkFromOverviewPage() {
        OverviewPageJourney.clickOverviewSection(OverviewSection.SubmitAndPay);
    }

    @Then("I should be on the permit fee page")
    public void iShouldBeOnThePermitFeePage() {
        world.permitFeePage.untilOnPage();
    }

    @Then("the page heading is displayed correctly on the fee page")
    public void thePageHeadingIsDisplayedCorrectlyOnTheFeePage() {
        world.permitFeePage.untilOnPage();
    }

    @Then("the Fee-breakdown sub-heading can be seen below the fee summary table")
    public void theFeeBreakingSubHeadingCanBeSeenBelowTheFeeSummaryTable() {
        String subHeading = getText("//h2[contains(text(),'Fee breakdown')]", SelectorType.XPATH);
        assertEquals("Fee breakdown", subHeading);
    }
}
