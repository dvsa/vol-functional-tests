package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourneySteps;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.permits.pages.OverviewPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;
import static org.dvsa.testing.lib.pages.BasePage.hasErrorMessagePresent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CertificateRequiredPageSteps implements En {

    public CertificateRequiredPageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the shortterm certificates required page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            OverviewPageJourneySteps.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
        });
        Then("^the certificates required page has the relevant information$", () -> {
            String heading = CertificatesRequiredPage.getPageHeading();
            assertEquals("Mandatory certificates for vehicles and trailers you intend to use", heading);
            assertTrue(CertificatesRequiredPage.isAdvisoryTextPresent());
            String expectedLicenceNumber = operatorStore.getCurrentLicenceNumber().orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber = BasePermitPage.getReferenceFromPage();
            Assert.assertTrue(actualReferenceNumber.contains(expectedLicenceNumber));
        });
        Then("^I should get the certificates required page error message$", () -> {
            String errorText = CertificatesRequiredPage.getErrorText();
            assertEquals(errorText, "Tick to confirm you understand that each vehicle and trailer must have the matching certificates.");
        });
        Then("^I confirm the Certificates Required checkbox$", CertificatesRequiredPage::confirmCertificateRequired);
        Then("^the user is navigated to the short term overview page with the status as completed$", () -> {
            String error = "Expected the status of certificates required page to be complete but it wasn't";
            OverviewPage.untilOnPage();
            OverviewPage.checkStatus(OverviewSection.CertificatesRequired,PermitStatus.COMPLETED);
        });

    }
}
