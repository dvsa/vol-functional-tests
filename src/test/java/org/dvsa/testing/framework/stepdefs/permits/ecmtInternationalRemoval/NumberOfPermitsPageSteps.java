
package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.external.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.NumberOfPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.enums.PermitStatus;
import org.dvsa.testing.framework.pageObjects.enums.OverviewSection;
import org.dvsa.testing.framework.pageObjects.external.pages.NumberOfPermitsPage;
import org.dvsa.testing.framework.pageObjects.external.pages.OverviewPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

public class NumberOfPermitsPageSteps implements En {

    public NumberOfPermitsPageSteps(World world) {
        When("^I am on the ECMT Removal number of permits page", () -> {
            EcmtInternationalRemovalJourney.completeUntilNumberOfPermitsPage(world);
        });
        And("^the page heading on the ECMT removals number of permits page is displayed correctly$", NumberOfPermitsPageJourney::hasECMTPageHeading);
        And("^the advisory text on the ECMT removals number of permits page is displayed correctly$", () -> {
            assertTrue(NumberOfPermitsPage.isFeeTextPresent());
        });

        And("^the application reference on the ECMT removals number of permits page is displayed correctly$", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            Assert.assertEquals(BasePermitJourney.getFullReferenceNumber(), actualReference);
        });
        And("^I enter number of permits more than the authorised vehicles and click save and continue$", () -> {
            NumberOfPermitsPage.exceedAuthorisedVehicle();
            BasePermitPage.saveAndContinue();
        });
        And("^I enter valid number of permits on the removals number of permits page and click save and continue$", () -> {
            NumberOfPermitsPage.enterAuthorisedVehicles();
            BasePermitPage.saveAndContinue();
        });
        And("^I enter valid number of permits on the removals number of permits page$", NumberOfPermitsPage::enterAuthorisedVehicles);
        Then("^the number of permits section on the ECMT Removals Overview page is complete$", () -> {
            OverviewPage.untilOnPage();
            OverviewPageJourney.checkStatus(OverviewSection.NumberOfPermits, PermitStatus.COMPLETED);
        });
    }
}