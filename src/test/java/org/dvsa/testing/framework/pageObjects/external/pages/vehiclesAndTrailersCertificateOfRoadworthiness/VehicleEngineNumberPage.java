package org.dvsa.testing.framework.pageObjects.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness;

import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BaseCertificateOfRoadWorthiness;


public class VehicleEngineNumberPage extends BaseCertificateOfRoadWorthiness {

    public static void enterEngineNumber() {
        enterTextIntoField("BD51SMR");
    }

}