package org.dvsa.testing.framework.Journeys.permits.pages;

import org.dvsa.testing.framework.Journeys.permits.BasePermitJourney;
import org.dvsa.testing.framework.pageObjects.enums.Tab;
import org.dvsa.testing.framework.pageObjects.external.pages.HomePage;

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
