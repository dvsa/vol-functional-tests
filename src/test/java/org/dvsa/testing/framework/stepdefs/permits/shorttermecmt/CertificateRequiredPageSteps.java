package org.dvsa.testing.framework.stepdefs.permits.shorttermecmt;

import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;

import static org.junit.Assert.assertEquals;

public class CertificateRequiredPageSteps implements En {

    public CertificateRequiredPageSteps(World world) {
        Then("^I should get the certificates required page error message$", () -> {
            String errorText = CertificatesRequiredPage.getErrorText();
            assertEquals(errorText, "Tick to confirm you understand that each vehicle and trailer must have the matching certificates.");
        });
        Then("^I confirm the Certificates Required checkbox$", CertificatesRequiredPage::confirmCertificateRequired);

    }
}
