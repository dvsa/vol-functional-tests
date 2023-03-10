package org.dvsa.testing.framework.stepdefs.permits.annualecmt;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Journeys.permits.EcmtApplicationJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.CertificatesRequiredPageJourney;
import org.dvsa.testing.framework.Journeys.permits.pages.RestrictedCountriesPageJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.*;

public class CertificatesRequiredPageSteps {

    private final World world;

    public CertificatesRequiredPageSteps(World world) {
        this.world = world;
    }

    @And("I am on the Ecmt Certificates required Page")
    public void iAmOnTheECMTCertificates() {
        world.ecmtApplicationJourney.completeUntilCertificatesRequiredPage();
    }

    @And("The application reference is displayed on the page")
    public void theApplicationReferenceIsDisplayed() {
        CertificatesRequiredPage.getReferenceFromPage();
    }

    @And("the certificates required page heading is as per the AC")
    public void theCertificatesRequiredPageHeading() {
        CertificatesRequiredPage.untilOnPage();
        String heading = CertificatesRequiredPage.getPageHeading();
        assertEquals("Mandatory certificates for vehicles and trailers you intend to use", heading);
    }

    @And("The advisory text contains bold characters at the right places")
    public void theAdvisoryTextContainsBoldCharacters() {
        assertTrue(CertificatesRequiredPage.isComplianceAndRoadworthinessFontIsBold());
    }

    @And("There is one checkbox with right label and not checked by default")
    public void thereIsOneCheckBoxWithRightLabel() {
        assertTrue(CertificatesRequiredPage.checkboxNotConfirmed());
    }

    @And("if I don't select the checkbox and click Save and Continue button")
    public void ifIDoNotSelectTheCheckBox() {
        BasePermitPage.saveAndContinue();
    }

    @And("if I select the checkbox and click Save and Return to Overview button")
    public void ifISelectTheCheckboxAndClickSave() {
        CertificatesRequiredPage.confirmCertificateRequired();
        BasePermitPage.clickReturnToOverview();
    }

    @And("I select the checkbox and click Save and Continue button")
    public void iSelectTheCheckBoxAndClick() {
        CertificatesRequiredPageJourney.completePage();
    }

    @Then("I am taken to the Restricted countries page")
    public void iAmTakenToTheRestrictedCountries() {
        RestrictedCountriesPageJourney.hasPageHeading();
    }
}