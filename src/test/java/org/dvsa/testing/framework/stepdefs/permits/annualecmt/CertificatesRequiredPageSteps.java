package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.external.pages.CheckIfYouNeedECMTPermitsPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.OverviewPageJourney;
import org.dvsa.testing.framework.Journeys.permits.external.pages.RestrictedCountriesPageJourney;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.external.pages.CabotagePage;
import org.dvsa.testing.lib.newPages.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.external.pages.baseClasses.BasePermitPage;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.CertificatesRequiredPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.RestrictedCountriesPageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CertificatesRequiredPageSteps implements En {

    public CertificatesRequiredPageSteps(World world) {

        And("^I am on the Ecmt Certificates required Page$", () -> {
            EcmtApplicationJourney.completeUntilCertificatesRequiredPage(world);
        });
        And("^The application reference is displayed on the page$", CertificatesRequiredPage::getReferenceFromPage);
        And("^the certificates required page heading is as per the AC$",() -> {
            CertificatesRequiredPage.untilOnPage();
            String heading = CertificatesRequiredPage.getPageHeading();
            assertEquals("Mandatory certificates for vehicles and trailers you intend to use", heading);
        });
        And("^The advisory text contains bold characters at the right places$", () -> {
            assertTrue(CertificatesRequiredPage.isComplianceAndRoadworthinessFontIsBold());
        });
        And("^There is one checkbox with right label and not checked by default$", () ->
            assertTrue(CertificatesRequiredPage.checkboxNotConfirmed())
        );
        And("^if I don't select the checkbox and click Save and Continue button$", BasePermitPage::saveAndContinue);
        And("^if I select the checkbox and click Save and Return to Overview button$", () -> {
            CertificatesRequiredPage.confirmCertificateRequired();
            BasePermitPage.clickReturnToOverview();
        });
        And("^I select the checkbox and click Save and Continue button$", CertificatesRequiredPageJourney::completePage);
        Then("^I am taken to the Restricted countries page$", RestrictedCountriesPageJourney::hasPageHeading);
    }
}