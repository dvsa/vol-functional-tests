package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.lib.newPages.enums.AdminOption;
import org.dvsa.testing.lib.newPages.internal.NavigationBar;

public class InternalBaseJourney {

    public static void navigateToAdminFeatureTogglePage() {
        NavigationBar.openAdminPanel();
        NavigationBar.clickAdministratorList(AdminOption.FEATURE_TOGGLE);
    }

    public static void navigateToAdminPermitsPage() {
        NavigationBar.openAdminPanel();
        NavigationBar.clickAdministratorList(AdminOption.PERMITS);
    }
}
