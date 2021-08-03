package org.dvsa.testing.framework.Journeys.permits.internal;

import Injectors.World;
import org.dvsa.testing.framework.Journeys.permits.external.BasePermitJourney;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.enums.PermitType;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsApplyPage;
import org.dvsa.testing.framework.pageObjects.internal.irhp.IrhpPermitsPage;
import org.dvsa.testing.framework.pageObjects.type.Permit;

import java.util.concurrent.TimeUnit;

public class IRHPPageJourney extends BasePermitJourney {

    private static World world;

    IRHPPageJourney(World world) {
        IRHPPageJourney.world = world;
    }

    public static void completeModal(PermitType permitType) {
        IrhpPermitsApplyPage.applyforPermit();
        IrhpPermitsPage.Model.untilModalIsPresent(Duration.LONG, TimeUnit.SECONDS);
        IrhpPermitsPage.Model.permitType(permitType);
        if (permitType == PermitType.ECMT_ANNUAL || permitType == PermitType.SHORT_TERM_ECMT) {
            waitAndSelectByIndex("Select a year","//select[@id='yearList']", SelectorType.XPATH,1);
        }
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
