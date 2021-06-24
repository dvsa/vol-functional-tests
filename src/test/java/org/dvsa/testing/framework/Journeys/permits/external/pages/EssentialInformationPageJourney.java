package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.permits.pages.EssentialInformationPage;
import org.junit.Assert;

public class EssentialInformationPageJourney extends BasePermitJourney {

    public static void hasPageHeading() {
        String heading = EssentialInformationPage.getPageHeading();
        Assert.assertEquals("Essential information", heading);
    }

    public static void completePage() {
        hasPageHeading();
        saveAndContinue();
    }
}
