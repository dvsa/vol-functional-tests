package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.*;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly.PermitStartDatePage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PermitStartDatePageSteps extends BasePage implements En {

    public PermitStartDatePageSteps(World world) {
        And("^I am on the ECMT removals permit start page$", () -> {
            EcmtInternationalRemovalJourney.completeApplicationUntilPermitStartDatePage(world);
        });
        And ("^the reference number is displayed correctly$", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(BasePermitPage.getReferenceNumber(), actualReference);
        });
        And ("^the page heading on permit start date page should be correct$", ()-> {
            String heading = PermitStartDatePage.getPageHeading();
            assertEquals("When do you need your permits to be valid from?", heading);
        });
        And ("^the advisory texts on permit start date page are displayed correctly$", () -> {
            assertTrue(PermitStartDatePage.checkAdvisoryTextPresent());
        });
        Then ("^the error message is displayed in the permit start date page$", () -> {
            String errorText = PermitStartDatePage.getErrorText();
            Assert.assertEquals("Enter a valid permit start date and include a day, month and year", errorText);
        });
        And ("^I am taken to the number of permits page$", () -> {
            isPath("/permits/application/\\d+/number-of-permits/");
        });
        When ("^I enter the valid date$", PermitStartDatePage::permitDate);
        When ("^I dont enter all the fields$", PermitStartDatePage::leaveDateBlank);
        When ("^I enter invalid date$", PermitStartDatePage::inValidDate);
        And ("^I enter a date ahead of 60 days$", PermitStartDatePage::dayAhead);
        And ("^I should get a valid error message$", () -> {
            assertTrue(PermitStartDatePage.checkDaysAheadErrorMessagePresent());
        });
    }
}
