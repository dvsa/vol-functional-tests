package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;

public class CertificatesRequiredPageJourney extends BasePermitJourney {

    public CertificatesRequiredPageJourney(World world){
        super(world);
    }
    public static void completePage() {
        CertificatesRequiredPage.untilOnPage();
        CertificatesRequiredPage.confirmCertificateRequired();
        BasePage.waitForElementToBePresent("//input[contains(@id,'_day')]");
        saveAndContinue();
        BasePage.waitForElementToBePresent("//input[contains(@id,'_day')]");
        BasePage.waitForElementNotToBePresent("//h1[contains(text(), 'Mandatory certificates')]");
    }
}
