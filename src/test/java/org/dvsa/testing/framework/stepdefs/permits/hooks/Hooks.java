package org.dvsa.testing.framework.stepdefs.permits.hooks;

import Injectors.World;
import activesupport.IllegalBrowserException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import org.dvsa.testing.framework.Journeys.permits.external.pages.InternalBaseJourney;
import org.dvsa.testing.framework.Utils.common.Util;
import org.dvsa.testing.lib.newPages.internal.admin.permits.FeatureTogglesPage;
import org.dvsa.testing.lib.newPages.internal.admin.permits.enums.FeatureToggleStatus;
import org.dvsa.testing.lib.newPages.internal.admin.permits.enums.Features;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.exception.ElementDidNotAppearWithinSpecifiedTimeException;
import org.dvsa.testing.lib.newPages.exception.ElementDidNotDisappearWithinSpecifiedTimeException;
import org.junit.jupiter.api.AfterAll;

import java.net.MalformedURLException;

public class Hooks extends BasePage {
    public static World world;

//    @After
//    public void afterScenario(Scenario scenario, World world) throws ElementDidNotAppearWithinSpecifiedTimeException {
//        if (scenario.isFailed())
//            Util.embedScreenShot(scenario);
//
//        if (scenario.getSourceTagNames().contains("@TOGGLE-ECMT")) {
//            // These enables ECMT before each scenario
//            world.APIJourneySteps.createAdminUser();
//            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
//// Why are they signing into internal at the end of some journeys?
//            InternalBaseJourney.navigateToAdminFeatureTogglePage();
//
//            FeatureTogglesPage.toggle(Features.PermitsAdmin, FeatureToggleStatus.Active);
//            FeatureTogglesPage.toggle(Features.InternalEcmt, FeatureToggleStatus.Active);
//            FeatureTogglesPage.toggle(Features.InternalPermits, FeatureToggleStatus.Active);
//            FeatureTogglesPage.toggle(Features.SelfserveEcmt, FeatureToggleStatus.Active);
//            FeatureTogglesPage.toggle(Features.SelfservePermits, FeatureToggleStatus.Active);
//            FeatureTogglesPage.toggle(Features.BackendEcmt, FeatureToggleStatus.Active);
//            FeatureTogglesPage.toggle(Features.BackendPermits, FeatureToggleStatus.Active);
//        }
//    }

    @AfterAll
    public static void tearDown() {
        quit();
    }
}