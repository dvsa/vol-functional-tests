package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import io.cucumber.java8.En;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.ECMTInternationalRemovalOnly.RemovalsEligibilityPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ECMTInternationalRemovalEligibiltyPageSteps implements En {
    public ECMTInternationalRemovalEligibiltyPageSteps(World world) {
        When("^I am on the ECMT International Removal Eligibity page", () -> {
            EcmtInternationalRemovalJourney.completeUntilRemovalEligibilityPage(world);
        });
        And("^the text is shown below the page heading$", () -> {
            String advisoryText = RemovalsEligibilityPage.getAdvisoryText();
            assertEquals("You may only use ECMT international removal permits for laden or empty journeys carried out by specialist removal companies to move household goods and business possessions.", advisoryText);
        });
        And("^the text is shown next to the tick box$", () -> {
            assertTrue(RemovalsEligibilityPage.isCheckboxAdvisoryTextPresent());
        });
        And("^I save and continue without selecting the checkbox$", BasePermitPage::saveAndContinue);
        When("^the checkbox is ticked$", RemovalsEligibilityPage::confirmCheckbox);
        Then("^the error message is displayed on ECMT Remove Eligibility Page$", () -> {
            assertTrue(RemovalsEligibilityPage.isErrorMessagePresent());
        });
        And("^the Application Number is shown correctly on ECMT International Eligibility page", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            assertEquals(BasePermitJourney.getFullReferenceNumber(), actualReference);
        });
        And("^the page heading is shown as per updated AC$", () -> {
            String heading = RemovalsEligibilityPage.getPageHeading();
            assertEquals("Removal permits can only be used for removal operations using specialised equipment and staff", heading);
        });
        Then("^I should be on the ECMT number of permits page$", NumberOfPermitsPageJourney::hasPageHeading);
    }
}