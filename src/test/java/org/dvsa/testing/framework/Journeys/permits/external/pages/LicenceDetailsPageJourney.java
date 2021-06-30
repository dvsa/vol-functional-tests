package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.lib.newPages.internal.details.BaseDetailsPage;
import org.dvsa.testing.lib.newPages.internal.details.enums.DetailsTab;

public class LicenceDetailsPageJourney {

    public static void clickIRHPTab(){
        BaseDetailsPage.Tab.hasTab(DetailsTab.IrhpPermits);
    }

    public static void clickFeesTab(){
        BaseDetailsPage.Tab.hasTab(DetailsTab.Fees);
    }

    public static void clickProcessingTab(){
        BaseDetailsPage.Tab.hasTab(DetailsTab.Processing);
    }
}
