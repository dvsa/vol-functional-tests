package org.dvsa.testing.framework.stepdefs.permits.internal;

import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.Journeys.permits.pages.InternalBaseJourney;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.dvsa.testing.framework.pageObjects.internal.NavigationBar;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.FeatureTogglesPage;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.FeatureToggleStatus;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.Features;
import org.junit.Assert;
import org.openqa.selenium.TimeoutException;

public class FeatureToggleSteps implements En {
    private World world;

    public FeatureToggleSteps(World world) {
        When("^I log in as an internal user with admin privileges$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        });
        Then("^I should be able to see the feature toggle option$", () -> {
            NavigationBar.openAdminPanel();
            NavigationBar.verifyOptionInList(AdminOption.FEATURE_TOGGLE);
        });
        When("^I log in as an internal user with normal privileges$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            // Need to add ways of creating users with normal privileges.
        });
        Then("^I should NOT be able to see the feature toggle option$", () -> {
            NavigationBar.openAdminPanel();
            boolean hasPermitToggle = true;

            try {
                NavigationBar.verifyOptionInList(AdminOption.FEATURE_TOGGLE);
            } catch (TimeoutException ex) {
                hasPermitToggle = false;
            }

            Assert.assertFalse("Expected permit toggle to not be present but was", hasPermitToggle);
        });
        And("^feature toggle for permits has been enabled$", () -> {
            InternalBaseJourney.navigateToAdminFeatureTogglePage();

            FeatureTogglesPage.toggle(Features.PermitsAdmin, FeatureToggleStatus.Active);
            FeatureTogglesPage.toggle(Features.InternalEcmt, FeatureToggleStatus.Active);
            FeatureTogglesPage.toggle(Features.InternalPermits, FeatureToggleStatus.Active);
            FeatureTogglesPage.toggle(Features.SelfserveEcmt, FeatureToggleStatus.Active);
            FeatureTogglesPage.toggle(Features.SelfservePermits, FeatureToggleStatus.Active);
            FeatureTogglesPage.toggle(Features.BackendEcmt, FeatureToggleStatus.Active);
            FeatureTogglesPage.toggle(Features.BackendPermits, FeatureToggleStatus.Active);
        });
        And("^disable all internal ECMT feature toggles$", () -> {
            InternalBaseJourney.navigateToAdminFeatureTogglePage();

            FeatureTogglesPage.toggle(Features.InternalEcmt, FeatureToggleStatus.Inactive);
            FeatureTogglesPage.toggle(Features.InternalPermits, FeatureToggleStatus.Inactive);
            FeatureTogglesPage.toggle(Features.BackendEcmt, FeatureToggleStatus.Inactive);
            FeatureTogglesPage.toggle(Features.BackendPermits, FeatureToggleStatus.Inactive);
        });
        And("^disable all external ECMT feature toggles$", () -> {
            InternalBaseJourney.navigateToAdminFeatureTogglePage();

            FeatureTogglesPage.toggle(Features.SelfserveEcmt, FeatureToggleStatus.Inactive);
            FeatureTogglesPage.toggle(Features.SelfservePermits, FeatureToggleStatus.Inactive);
        });
    }
}
