package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.external.ShorttermECMTJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.lib.enums.PermitStatus;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.enums.PeriodType;
import org.dvsa.testing.lib.newPages.enums.PermitUsage;
import org.dvsa.testing.lib.newPages.permits.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.permits.pages.ECMTAndShortTermECMTOnly.YearSelectionPage;
import org.dvsa.testing.lib.newPages.permits.pages.PermitUsagePage;
import org.dvsa.testing.lib.pages.external.permit.BasePermitPage;
import org.junit.Assert;

import static org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps.clickToPermitTypePage;

public class CertificateRequiredPageSteps implements En {

    public CertificateRequiredPageSteps(OperatorStore operatorStore, World world) {

        Then("^I am on the shortterm certificates required page$", () -> {
            clickToPermitTypePage(world);
            ShorttermECMTJourney.getInstance().permitType(PermitType.SHORT_TERM_ECMT, operatorStore);
            YearSelectionPage.selectShortTermValidityPeriod();
            ShorttermECMTJourney.getInstance().shortTermType(PeriodType.ShortTermECMTAPSGWithSectors,operatorStore);
            ShorttermECMTJourney.getInstance().licencePage(operatorStore,world);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.HowWillYouUseThePermits);
            PermitUsagePage.permitUsage(PermitUsage.random());
            BasePermitPage.saveAndContinue();
            CabotagePage.confirmWontUndertakeCabotage();
            BasePermitPage.saveAndContinue();
        });
        Then("^the certificates required page has the relevant information$", () -> {
            CertificatesRequiredPage.hasPageHeading();
            CertificatesRequiredPage.hasAdvisoryTexts();
            String expectedLicenceNumber= operatorStore.getCurrentLicenceNumber().orElseThrow(IllegalAccessError::new);
            String actualReferenceNumber= BasePermitPage.getReferenceFromPage();
            Assert.assertTrue(actualReferenceNumber.contains(expectedLicenceNumber));
        });
        Then("^I should get the certificates required page error message$", org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage::hasErrorText);
        Then("^I confirm the Certificates Required checkbox$", org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage::confirmCertificateRequired);
        Then("^the user is navigated to the short term overview page with the status as completed$", () -> {
            String error = "Expected the status of certificates required page to be complete but it wasn't";
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.untilOnPage();
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.checkStatus(OverviewSection.CertificatesRequired,PermitStatus.COMPLETED);
        });

    }
}
