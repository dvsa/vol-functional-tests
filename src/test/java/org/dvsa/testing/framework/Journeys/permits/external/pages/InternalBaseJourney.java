package org.dvsa.testing.framework.Journeys.permits.external.pages;

import org.dvsa.testing.lib.newPages.enums.AdminOption;
import org.dvsa.testing.lib.newPages.exception.ElementDidNotAppearWithinSpecifiedTimeException;
import org.dvsa.testing.lib.newPages.internal.NavigationBar;

public class InternalBaseJourney {

    public static void navigateToAdminFeatureTogglePage() throws ElementDidNotAppearWithinSpecifiedTimeException {
        NavigationBar.openAdminPanel();
        NavigationBar.clickAdministratorList(AdminOption.FEATURE_TOGGLE);
    }

    public static void navigateToAdminPermitsPage() throws ElementDidNotAppearWithinSpecifiedTimeException {
        NavigationBar.openAdminPanel();
        NavigationBar.clickAdministratorList(AdminOption.PERMITS);
    }
}
