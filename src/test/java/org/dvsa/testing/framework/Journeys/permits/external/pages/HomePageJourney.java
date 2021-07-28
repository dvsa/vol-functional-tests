package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.newPages.enums.Tab;
import org.dvsa.testing.lib.newPages.external.pages.HomePage;

public class HomePageJourney extends BasePermitJourney {

    public static void selectPermitTab() {
        HomePage.selectTab(Tab.PERMITS);
    }

    public static void beginPermitApplication() {
        HomePageJourney.selectPermitTab();
        HomePage.applyForLicenceButton();
    }

    public static void payAllOutstandingFees() {
        HomePage.FeesTab.selectAllOutstandingFees();
        HomePage.FeesTab.pay();
        HomePage.FeesTab.payNowButton();
    }
}
