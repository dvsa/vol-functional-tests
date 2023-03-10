package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CertificateRequiredPageSteps {
        @Then("I should get the certificates required page error message")
        public void iShouldGetTheCertificatedRequired()
        {
            String errorText = CertificatesRequiredPage.getErrorText();
            assertEquals(errorText, "Tick to confirm you understand that each vehicle and trailer must have the matching certificates.");
        }
        @Then("I confirm the Certificates Required checkbox")
        public void iConfirmTheCertificatesRequired()
        {
            CertificatesRequiredPage.confirmCertificateRequired();
    }
}