package org.dvsa.testing.framework.stepdefs.permits.hooks;

import activesupport.IllegalBrowserException;
import activesupport.system.Properties;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import org.dvsa.testing.framework.Journeys.permits.internal.BaseInternalJourney;
import org.dvsa.testing.framework.Utils.common.Util;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.Action;
import org.dvsa.testing.lib.pages.enums.AdminOption;
import org.dvsa.testing.lib.pages.exception.ElementDidNotAppearWithinSpecifiedTimeException;
import org.dvsa.testing.lib.pages.exception.ElementDidNotDisappearWithinSpecifiedTimeException;
import org.dvsa.testing.lib.pages.internal.NavigationBar;
import org.dvsa.testing.lib.pages.internal.admin.permits.FeatureTogglesPage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;
import org.junit.jupiter.api.AfterAll;

import java.net.MalformedURLException;

public class Hooks extends BasePage {
    @After
    public void afterScenario(Scenario scenario) throws ElementDidNotDisappearWithinSpecifiedTimeException, ElementDidNotAppearWithinSpecifiedTimeException, MalformedURLException, IllegalBrowserException {
        if (scenario.isFailed())
            Util.embedScreenShot(scenario);

        if (scenario.getSourceTagNames().contains("@TOGGLE-ECMT")) {
            // These enables ECMT before each scenario
            deleteCookies();
            refreshPage();
            get(URL.build(ApplicationType.INTERNAL, Properties.get("env", true), "auth/login/").toString());

            BaseInternalJourney.getInstance().signin(BaseInternalJourney.User.Admin);

            NavigationBar.adminPanel(Action.OPEN);
            NavigationBar.administratorList(AdminOption.FEATURE_TOGGLE);

            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.PermitsAdmin, FeatureTogglesPage.Status.Active);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.InternalEcmt, FeatureTogglesPage.Status.Active);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.InternalPermits, FeatureTogglesPage.Status.Active);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.SelfserveEcmt, FeatureTogglesPage.Status.Active);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.SelfservePermits, FeatureTogglesPage.Status.Active);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.BackendEcmt, FeatureTogglesPage.Status.Active);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.BackendPermits, FeatureTogglesPage.Status.Active);
        }
    }

    @AfterAll
    public static void tearDown() throws MalformedURLException, IllegalBrowserException {
        quit();
    }
}