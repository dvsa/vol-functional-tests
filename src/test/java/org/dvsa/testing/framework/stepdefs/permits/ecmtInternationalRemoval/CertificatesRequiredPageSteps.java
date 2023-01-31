package org.dvsa.testing.framework.stepdefs.permits.ecmtInternationalRemoval;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Journeys.permits.EcmtInternationalRemovalJourney;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

import static org.junit.jupiter.api.Assertions.*;

public class CertificatesRequiredPageSteps extends BasePage {
    World world;

    public CertificatesRequiredPageSteps(World world) {
        this.world = world;
    }

    @And("I am on the ECMT Removals certificates required page")
    public void iAmOnTheECMTRemovalsCertificatesRequiredPAge() {
        EcmtInternationalRemovalJourney.completeUntilCertificatesRequiredPage(world);
    }

    @And("the application reference number is displayed")
    public void theApplicationReferenceNumberIsDisplayed() {
        String actualReference = BasePermitPage.getReferenceFromPage();
        assertEquals(BasePermitJourney.getFullReferenceNumber(), actualReference);
    }

    @And("the correct text is displayed next to the checkbox")
    public void theCorrectTextIsDisplayedNextToTheCheckbox() {
        assertTrue(CertificatesRequiredPage.isCheckboxTextPresent());
    }

    @And("I am on the ecmt removals permit start date page")
    public void iAmOnTheECMTRemovalsPermitStartDatePage() {
        isPath("/application/\\d+/permit-start-date/");
    }

    @And("on the certificates required page advisory texts are displayed")
    public void onTheCertificatesRequiredPageAdvisoryTextsAreDisplayed() {
        assertEquals("If your permit application is successful, you are required to have the appropriate Certificate of Compliance and Certificate of Roadworthiness for each vehicle and trailer you intend to use.", CertificatesRequiredPage.getAdvisoryText());
    }
}