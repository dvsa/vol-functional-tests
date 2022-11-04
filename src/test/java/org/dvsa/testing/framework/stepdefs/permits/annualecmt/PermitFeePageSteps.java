package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import io.cucumber.java8.En;;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.OverviewPageJourney;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.PermitFeePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermitFeePageSteps extends BasePermitPage implements En {
    public PermitFeePageSteps(World world) {

        And("^I select the submit and pay link from overview page$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.SubmitAndPay);
        });
        Then("^I should be on the permit fee page$", PermitFeePage::untilOnPage);
        Then("^the page heading is displayed correctly on the fee page$", PermitFeePage::untilOnPage);
        Then("^the Fee-breakdown sub-heading can be seen below the fee summary table$", () -> {
            String subHeading = getText("//h2[contains(text(),'Fee breakdown')]", SelectorType.XPATH);
            assertEquals("Fee breakdown", subHeading);
        });
    }
}