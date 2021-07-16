package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Utils.store.LicenceStore;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.external.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.CountriesWithLimitedPermitsPage;
import org.dvsa.testing.lib.newPages.external.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.external.pages.PermitUsagePage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CountriesWithLimitedPermitsPageSteps implements En {

    public CountriesWithLimitedPermitsPageSteps (OperatorStore operatorStore, World world) {
        And("^I am on short term countries with limited permits page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore)
                    .licencePage(operatorStore,world);
            LicenceStore licence = operatorStore.getLatestLicence().orElseGet(LicenceStore::new);
            operatorStore.withLicences(licence);
            OverviewPageJourney.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            licence.getEcmt().setPermitUsage(PermitUsage.random());
            PermitUsagePage.permitUsage(licence.getEcmt().getPermitusage());
            BasePermitPage.saveAndContinue();
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
            CertificatesRequiredPage.completePage();
        });
        Then("^the application reference number on countries with limited permits page is shown correctly$", () -> {
            String expectedLicenceNumber = operatorStore.getCurrentLicenceNumber().orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber = BasePermitPage.getReferenceFromPage();
            Assert.assertThat(actualReferenceNumber, containsString(expectedLicenceNumber));
        });
        And("^the page heading on short term  countries with limited countries page is Shown Correctly$", () -> {
            String heading = CountriesWithLimitedPermitsPage.getPageHeading();
            assertEquals("Will you be transporting goods to Greece, Hungary, Italy or Russia?", heading);
        });
        And("^the advisory text on short term countries with limited countries page is Shown Correctly$", () -> {
            assertTrue(CountriesWithLimitedPermitsPage.isAdvisoryTextPresent());
        });
        And ("^I select save and continue without confirming$", BasePermitPage::saveAndContinue);
        And ("^I should get the relevant error message$", () -> {
            String errorText = CountriesWithLimitedPermitsPage.getErrorText();
            assertEquals("Value is required", errorText);
        });
        And ("^I select save and return to overview link without confirming$", BasePermitPage::clickReturnToOverview);
        And ("^I select the countries with limited permits hyperlink$", () -> {
            OverviewPageJourney.clickOverviewSection(OverviewSection.CountriesWithLimitedPermits);
        });
        And ("^I should be on the countries with limited permits page$", CountriesWithLimitedPermitsPage::untilOnPage);
        When ("^I have selected some short term countries with limited permits and clicked save and continue$", CountriesWithLimitedPermitsPage::noCountriesWithLimitedPermits);
    }
}