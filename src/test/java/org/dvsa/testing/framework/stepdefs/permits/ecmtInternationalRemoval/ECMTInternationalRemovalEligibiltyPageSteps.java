package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import Injectors.World;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTInternationalRemovalOnly.RemovalsEligibilityPage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class ECMTInternationalRemovalEligibiltyPageSteps implements En {
    public ECMTInternationalRemovalEligibiltyPageSteps(OperatorStore operatorStore, World world) {
        When("^I am on the ECMT International Removal Eligibity page", () -> {
            clickToPermitTypePage(world);
            EcmtInternationalRemovalJourney.getInstance()
                    .permitType(PermitType.ECMT_INTERNATIONAL_REMOVAL, operatorStore)
                    .licencePage(operatorStore, world);
            EcmtInternationalRemovalJourney.getInstance()
                    .overview(OverviewSection.RemovalsEligibility);
        });
        And ("^the text is shown below the page heading$", RemovalsEligibilityPage::hasAdvisoryText);
        And ("^the text is shown next to the tick box$", () -> {
           Assert.assertTrue(RemovalsEligibilityPage.hasCheckboxAdvisoryText());
        });
        And ("^I save and return to overview without selecting the checkbox$", BasePermitPage::overview);
        And ("^I save and continue without selecting the checkbox$", BasePermitPage::saveAndContinue);
        When("^the checkbox is ticked$", RemovalsEligibilityPage::confirmCheckbox);
        Then("^the error message is displayed on ECMT Remove Eligibility Page$", () -> {
            RemovalsEligibilityPage.hasErrorMessage();
        });
        And ("^the Application Number is shown correctly on ECMT International Eligibility page", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(operatorStore.getLatestLicence().get().getReferenceNumber(), actualReference);
        });
        And ("^the page heading is shown as per updated AC$", RemovalsEligibilityPage::hasPageHeading);
    }
  }
