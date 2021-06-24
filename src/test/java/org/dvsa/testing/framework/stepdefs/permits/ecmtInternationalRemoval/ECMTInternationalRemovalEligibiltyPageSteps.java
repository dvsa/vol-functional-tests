package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTInternationalRemovalOnly.RemovalsEligibilityPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ECMTInternationalRemovalEligibiltyPageSteps implements En {
    public ECMTInternationalRemovalEligibiltyPageSteps(OperatorStore operatorStore, World world) {
        When("^I am on the ECMT International Removal Eligibity page", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
                    .licencePage(operatorStore, world);
            OverviewPageJourney.clickOverviewSection(OverviewSection.RemovalsEligibility);
        });
        And ("^the text is shown below the page heading$", () -> {
            String advisoryText = RemovalsEligibilityPage.getAdvisoryText();
            assertEquals("You may only use ECMT international removal permits for laden or empty journeys carried out by specialist removal companies to move household goods and business possessions.", advisoryText);
        });
        And ("^the text is shown next to the tick box$", () -> {
           Assert.assertTrue(RemovalsEligibilityPage.isCheckboxAdvisoryTextPresent());
        });
        And ("^I save and return to overview without selecting the checkbox$", BasePermitPage::overview);
        And ("^I save and continue without selecting the checkbox$", BasePermitPage::saveAndContinue);
        When("^the checkbox is ticked$", RemovalsEligibilityPage::confirmCheckbox);
        Then("^the error message is displayed on ECMT Remove Eligibility Page$", () -> {
            assertTrue(RemovalsEligibilityPage.isErrorMessagePresent());
        });
        And ("^the Application Number is shown correctly on ECMT International Eligibility page", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);
        });
        And ("^the page heading is shown as per updated AC$", () -> {
            String heading = RemovalsEligibilityPage.getPageHeading();
            assertEquals("Removal permits can only be used for removal operations using specialised equipment and staff", heading);
        });
    }
  }
