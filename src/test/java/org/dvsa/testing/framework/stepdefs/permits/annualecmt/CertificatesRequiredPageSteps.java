package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Utils.store.OperatorStore;
import org.dvsa.testing.framework.stepdefs.permits.common.CommonSteps;
import org.dvsa.testing.lib.newPages.enums.OverviewSection;
import org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage;
import org.dvsa.testing.lib.newPages.permits.pages.CheckIfYouNeedECMTPermitsPage;
import org.dvsa.testing.lib.pages.external.permit.*;

import static org.junit.Assert.assertTrue;

public class CertificatesRequiredPageSteps implements En {

    public CertificatesRequiredPageSteps(World world, OperatorStore operatorStore) {

        And("^I am on the Ecmt Certificates required Page$", () -> {
            CommonSteps.beginEcmtApplicationAndGoToOverviewPage(world, operatorStore);
            org.dvsa.testing.lib.newPages.permits.pages.OverviewPage.clickOverviewSection(OverviewSection.CheckIfYouNeedPermits);
            CheckIfYouNeedECMTPermitsPage.checkNeedECMTPermits();
            CheckIfYouNeedECMTPermitsPage.saveAndContinue();
            CabotagePage.wontCarryCabotage(true);
        });
        And("^The application reference is displayed on the page$",() -> {
                CertificatesRequiredPage.getReferenceFromPage();
        });
        And("^The main page heading is as per the AC$",() -> org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.hasPageHeading());
        And("^Correct advisory text is shown below the page heading$", () -> org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.hasAdvisoryTexts());
        And("^The advisory text contains bold characters at the right places$", () -> org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.assertComplianceAndRoadworthinessFontIsBold());
        And("^There is one checkbox with right label and not checked by default$", () -> {
            assertTrue(org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.checkboxNotConfirmed());
        });
        And("^if I don't select the checkbox and click Save and Continue button$", () -> BasePermitPage.saveAndContinue());
        Then("^I am presented with a validation error message$", () -> org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.hasErrorText());
        And("^if I don't select the checkbox and click Save and Return to Overview button$", () -> BasePermitPage.overview());
        Then("^I am presented with same validation error message$", () -> org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.hasErrorText());
        And("^if I select the checkbox and click Save and Return to Overview button$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.confirmCertificateRequired();
            BasePermitPage.overview();
        });
        And("^I select the checkbox and click Save and Continue button$", () -> {
            org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.confirmCertificateRequired();
            org.dvsa.testing.lib.newPages.permits.pages.CertificatesRequiredPage.saveAndContinue();
        });
        Then("^I am taken to the Restricted countries page$", () -> org.dvsa.testing.lib.newPages.permits.pages.RestrictedCountriesPage.hasECMTPageHeading());
    }
}