package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;

public class CertificatesRequiredPageJourney extends BasePermitJourney {

    public static void completePage() {
        CertificatesRequiredPage.confirmCertificateRequired();
        saveAndContinue();
    }
}
