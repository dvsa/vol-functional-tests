package org.dvsa.testing.framework.Journeys.permits.internal;

import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.lib.enums.Duration;
import org.dvsa.testing.lib.enums.PermitType;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.lib.newPages.internal.irhp.IrhpPermitsPage;

import java.util.concurrent.TimeUnit;

public class IRHPPageJourney extends BasePermitJourney {

    private static World world;

    IRHPPageJourney(World world) {
        IRHPPageJourney.world = world;
    }

    public static void completeModal(PermitType permitType) {
        IrhpPermitsPage.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);
        IrhpPermitsPage.Model.permitType(permitType);
        IrhpPermitsPage.Model.continueButton();
        IrhpPermitsPage.Model.untilModalIsGone();
    }

    public static void logInToInternalAndIRHPGrantApplication(World world) {
        world.APIJourney.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        world.internalNavigation.urlSearchAndViewLicence();
        String browser = String.valueOf(getURL());
        get(browser+"irhp-application/");
        IrhpPermitsApplyPage.viewApplication();
        IrhpPermitsApplyPage.grantApplication();
        IrhpPermitsApplyPage.continueButton();
    }
}
