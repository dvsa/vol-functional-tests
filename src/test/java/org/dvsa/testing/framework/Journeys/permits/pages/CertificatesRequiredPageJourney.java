package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.CertificatesRequiredPage;

public class CertificatesRequiredPageJourney extends BasePermitJourney {

    public CertificatesRequiredPageJourney(World world){
        super(world);
    }
    public static void completePage() {
        CertificatesRequiredPage.untilOnPage();
        waitForElementToBeClickable("//input[@id='qaElement']", SelectorType.XPATH);
        clickByXPath("//input[@id='qaElement']");
        UniversalActions.clickSaveAndContinue();
    }

}
