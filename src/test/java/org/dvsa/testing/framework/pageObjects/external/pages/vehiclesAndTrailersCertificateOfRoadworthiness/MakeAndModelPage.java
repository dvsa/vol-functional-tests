package org.dvsa.testing.framework.pageObjects.external.pages.vehiclesAndTrailersCertificateOfRoadworthiness;

import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BaseCertificateOfRoadWorthiness;


public class MakeAndModelPage extends BaseCertificateOfRoadWorthiness {

    public static void enterMakeAndModel(String makeAndModel) {
        enterTextIntoField(makeAndModel);
    }

}