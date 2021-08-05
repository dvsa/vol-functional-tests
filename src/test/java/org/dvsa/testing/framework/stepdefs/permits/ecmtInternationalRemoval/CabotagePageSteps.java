package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.CabotagePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;

public class CabotagePageSteps extends BasePage implements En {
    public CabotagePageSteps(World world) {
        And("^I am on the ECMT International cabotage Page$", () -> {
            EcmtInternationalRemovalJourney.completeApplicationUntilCabotagePage(world);
        });
        And ("^the ECMT International Removal application reference number should be displayed$", () -> {
            Assert.assertEquals(BasePermitJourney.getReferenceNumber(), CabotagePage.getReferenceFromPage());
        });
        Then("^the ECMT international removal cabotage heading should be correct$", () -> {
            String heading = CabotagePage.getPageHeading();
            assertEquals("Removal permits do not allow you to carry out cabotage", heading);
        });
        Then ("^the correct text is displayed next to the checkbox in ECMT Removal cabotage page", () -> {
            //TODO: Previous code didn't make any sense being here.
        });
        When("^save and continue  button is selected without selecting the checkbox$", BasePermitPage::saveAndContinue);
        Then("^I should be taken to certificates required page", () -> {
            Assert.assertTrue(isPath("/permits/application/\\d+/st-certificates/"));
        });
    }
}
