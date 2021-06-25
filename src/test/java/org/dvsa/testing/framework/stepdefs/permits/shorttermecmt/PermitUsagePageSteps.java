package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.PermitUsagePageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.PermitUsagePage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.junit.Assert.*;

public class PermitUsagePageSteps extends BasePermitPage implements En {

    public PermitUsagePageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the shortterm permit usage page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore)
                    .licencePage(operatorStore,world);
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);
            OverviewPageJourney.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
        });
        Then("^the shortterm ecmt permit usage page has an application reference number$", () -> {
            String actualReference = BasePermitPage.getReferenceFromPage();
            assertNotNull(actualReference);
            String aa = operatorStore.getCurrentLicenceNumber().toString();
            assertNotNull(aa);
        });
        And("^the page heading on the permit usage page is displayed correctly$", PermitUsagePageJourney::hasPageHeading);
        And("^the short term ecmt permit usage buttons are displayed  unselected by default$", () -> {
            Assert.assertTrue("not selected", !isSelected("//input[@type='radio']", SelectorType.XPATH));
        });

        Then("^the shortterm ecmt permit usage page has advisory messages$", () -> {
            PermitUsagePage.hasAdvisoryMessages();
            assertTrue(PermitUsagePage.isAdvisoryTextPresent());
            assertTrue(PermitUsagePage.isLinkNotPresent());
        });

        And("^when I save and continue without selecting any radio button$", BasePermitPage::saveAndContinue);

        Then("^I should get error message on the permit usage page$", () -> {
            assertEquals("Please select one option", PermitUsagePage.getErrorText());
        });

        And("^when I save and return to overview without selecting any radio button$", BasePermitPage::saveAndContinue);

        Then("^I should be on the short term ECMT overview page$", () -> {
            Assert.assertTrue(isPath("/permits/application/\\d+/"));
        });

        When("^I confirm the permit usage$", () -> PermitUsagePage.permitUsage(PermitUsage.random()));

        Then("^the user is navigated to the overview page with the permits usage section status displayed as completed$", () -> {
            OverviewPageJourney.checkStatus(OverviewSection.HowWillYouUseThePermits, PermitStatus.COMPLETED);
        });

    }
}
