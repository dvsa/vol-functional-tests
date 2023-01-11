package org.dvsa.testing.framework.stepdefs.permits.internal;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Journeys.permits.pages.InternalBaseJourney;
import org.dvsa.testing.framework.pageObjects.enums.AdminOption;
import org.dvsa.testing.framework.pageObjects.internal.NavigationBar;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.FeatureTogglesPage;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.FeatureToggleStatus;
import org.dvsa.testing.framework.pageObjects.internal.admin.permits.enums.Features;
import org.openqa.selenium.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class FeatureToggleSteps {
    private final World world;

    public FeatureToggleSteps(World world) {
        this.world = world;
    }

    @When("I log in as an internal user with admin privileges")
    public void iLogInAsAnInternalUserWithAdminPrivileges() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
    }

    @Then("I should be able to see the feature toggle option")
    public void iShouldBeAbleToSeeTheFeatureToggleOption() {
        NavigationBar.openAdminPanel();
        NavigationBar.verifyOptionInList(AdminOption.FEATURE_TOGGLE);
    }

    @When("I log in as an internal user with normal privileges")
    public void iLogInAsAnInternalUserWithNormalPrivileges() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        // Need to add ways of creating users with normal privileges.
    }

    @Then("I should NOT be able to see the feature toggle option")
    public void iShouldNOTBeAbleToSeeTheFeatureToggleOption() {
        NavigationBar.openAdminPanel();
        boolean hasPermitToggle = true;

        try {
            NavigationBar.verifyOptionInList(AdminOption.FEATURE_TOGGLE);
        } catch (TimeoutException ex) {
            hasPermitToggle = false;
        }

        assertFalse(hasPermitToggle,"Expected permit toggle to not be present but was");
    }

    @And("feature toggle for permits has been enabled")
    public void featureToggleForPermitsHasBeenEnabled() {
        InternalBaseJourney.navigateToAdminFeatureTogglePage();

        FeatureTogglesPage.toggle(Features.PermitsAdmin, FeatureToggleStatus.Active);
        FeatureTogglesPage.toggle(Features.InternalEcmt, FeatureToggleStatus.Active);
        FeatureTogglesPage.toggle(Features.InternalPermits, FeatureToggleStatus.Active);
        FeatureTogglesPage.toggle(Features.SelfserveEcmt, FeatureToggleStatus.Active);
        FeatureTogglesPage.toggle(Features.SelfservePermits, FeatureToggleStatus.Active);
        FeatureTogglesPage.toggle(Features.BackendEcmt, FeatureToggleStatus.Active);
        FeatureTogglesPage.toggle(Features.BackendPermits, FeatureToggleStatus.Active);
    }

    @And("disable all internal ECMT feature toggles")
    public void disableAllInternalECMTFeatureToggles() {
        InternalBaseJourney.navigateToAdminFeatureTogglePage();

        FeatureTogglesPage.toggle(Features.InternalEcmt, FeatureToggleStatus.Inactive);
        FeatureTogglesPage.toggle(Features.InternalPermits, FeatureToggleStatus.Inactive);
        FeatureTogglesPage.toggle(Features.BackendEcmt, FeatureToggleStatus.Inactive);
        FeatureTogglesPage.toggle(Features.BackendPermits, FeatureToggleStatus.Inactive);
    }

    @And("disable all external ECMT feature toggles")
    public void disableAllExternalECMTFeatureToggles() {
        InternalBaseJourney.navigateToAdminFeatureTogglePage();

        FeatureTogglesPage.toggle(Features.SelfserveEcmt, FeatureToggleStatus.Inactive);
        FeatureTogglesPage.toggle(Features.SelfservePermits, FeatureToggleStatus.Inactive);
    }
}