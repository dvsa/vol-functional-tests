package org.dvsa.testing.framework.pageObjects.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness;

import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BaseCertificateOfRoadWorthiness;


public class VehicleRegistrationNumberPage extends BaseCertificateOfRoadWorthiness {

    public static String getRequiredFieldValidation() {
        return getText("//p[contains(text(),'Enter the registration number plate')]", SelectorType.XPATH);
    }

    public static void enterRegistrationNumber() {
        enterTextIntoField("BD51SMR");
    }

    public static void enterTrailerRegistrationNumber() {
        enterTextIntoField("B1234567");
    }

}