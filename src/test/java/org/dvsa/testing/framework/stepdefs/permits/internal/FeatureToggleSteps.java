package org.dvsa.testing.framework.stepdefs.permits.internal;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.newPages.enums.Action;
import org.dvsa.testing.lib.newPages.enums.AdminOption;
import org.dvsa.testing.lib.pages.internal.NavigationBar;
import org.dvsa.testing.lib.pages.internal.admin.permits.FeatureTogglesPage;
import org.junit.Assert;
import org.openqa.selenium.TimeoutException;

public class FeatureToggleSteps implements En {
    private World world;

    public FeatureToggleSteps(World world) {
        When("^I log in as an internal user with admin privileges$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());        });
        Then("^I should be able to see the feature toggle option$", () -> {
            NavigationBar.adminPanel(Action.OPEN);
            NavigationBar.verifyOptionInList(AdminOption.FEATURE_TOGGLE);
        });
        When("^I log in as an internal user with normal privileges$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            // Need to add ways of creating users with normal privileges.
        });
        Then("^I should NOT be able to see the feature toggle option$", () -> {
            NavigationBar.adminPanel(Action.OPEN);
            boolean hasPermitToggle = true;

            try {
                NavigationBar.verifyOptionInList(AdminOption.FEATURE_TOGGLE);
            } catch (TimeoutException ex) {
                hasPermitToggle = false;
            }

            Assert.assertFalse("Expected permit toggle to not be present but was", hasPermitToggle);
        });
        And("^feature toggle for permits has been enabled$", () -> {
            NavigationBar.adminPanel(Action.OPEN);
            NavigationBar.administratorList(AdminOption.FEATURE_TOGGLE);

            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.PermitsAdmin, FeatureTogglesPage.Status.Active);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.InternalEcmt, FeatureTogglesPage.Status.Active);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.InternalPermits, FeatureTogglesPage.Status.Active);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.SelfserveEcmt, FeatureTogglesPage.Status.Active);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.SelfservePermits, FeatureTogglesPage.Status.Active);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.BackendEcmt, FeatureTogglesPage.Status.Active);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.BackendPermits, FeatureTogglesPage.Status.Active);
        });
        And("^disable all internal ECMT feature toggles$", () -> {
            NavigationBar.adminPanel(Action.OPEN);
            NavigationBar.administratorList(AdminOption.FEATURE_TOGGLE);

            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.InternalEcmt, FeatureTogglesPage.Status.Inactive);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.InternalPermits, FeatureTogglesPage.Status.Inactive);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.BackendEcmt, FeatureTogglesPage.Status.Inactive);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.BackendPermits, FeatureTogglesPage.Status.Inactive);
        });
        And("^disable all external ECMT feature toggles$", () -> {
            NavigationBar.adminPanel(Action.OPEN);
            NavigationBar.administratorList(AdminOption.FEATURE_TOGGLE);

            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.SelfserveEcmt, FeatureTogglesPage.Status.Inactive);
            FeatureTogglesPage.toggle(FeatureTogglesPage.Feature.SelfservePermits, FeatureTogglesPage.Status.Inactive);
        });
    }
}
