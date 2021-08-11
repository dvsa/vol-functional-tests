package org.dvsa.testing.framework.pageObjects.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness;

import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BaseCertificateOfRoadWorthiness;


public class VehicleIdentificationNumberPage extends BaseCertificateOfRoadWorthiness {

    public static void enterIdentificationNumber() {
        enterTextIntoField("BD51SMR");
    }
}