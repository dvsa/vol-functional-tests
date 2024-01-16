package org.dvsa.testing.framework.pageObjects.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness;

import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BaseCertificateOfRoadWorthiness;


public class VehicleEngineTypePage extends BaseCertificateOfRoadWorthiness {

    public static void enterEngineType() {
        enterTextIntoField("BD51SMR");
    }

}