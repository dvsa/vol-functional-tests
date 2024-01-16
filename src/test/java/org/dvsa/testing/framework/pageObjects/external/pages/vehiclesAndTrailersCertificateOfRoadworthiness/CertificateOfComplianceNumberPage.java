package org.dvsa.testing.framework.pageObjects.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness;

import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BaseCertificateOfRoadWorthiness;


public class CertificateOfComplianceNumberPage extends BaseCertificateOfRoadWorthiness {

    public static void enterComplianceNumber(String complianceNumber) {
        enterTextIntoField(complianceNumber);
    }

}