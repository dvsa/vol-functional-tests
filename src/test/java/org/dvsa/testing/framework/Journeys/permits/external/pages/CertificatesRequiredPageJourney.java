package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;

public class CertificatesRequiredPageJourney extends BasePermitJourney {

    public static void completePage() {
        CertificatesRequiredPage.confirmCertificateRequired();
        saveAndContinue();
    }
}
