package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

public class InternalSearch extends BasePage implements En {

    public InternalSearch(World world) {

        When("^i search for and click on my licence$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.internalSearch.searchAndViewLicence();
        });
        And("^i search for and click on my application", () -> {
            if (isElementPresent("//select[@id='search-select']", SelectorType.XPATH)) {
                world.internalSearch.searchAndViewApplication();
            } else {
                world.APIJourneySteps.createAdminUser();
                world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
                world.internalSearch.searchAndViewApplication();
            }
        });
        When("^i search for and click on my case", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.internalSearch.searchAndViewCase();
        });
        When("^i search for my psv disc and click on my licence and discs$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.internalSearch.searchAndViewPSVDisc();
        });
        When("^i search for my address and click on my licence and addresses$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.internalSearch.searchAndViewAddress();
        });

        And("^i create an admin and url search for my licence$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.internalNavigation.urlSearchAndViewLicence();
        });
        And("^i create an admin and url search for my application", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.internalNavigation.urlSearchAndViewApplication();
        });
        And("^i create an admin and url search for my variation", () -> {
            world.APIJourneySteps.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.internalNavigation.urlSearchAndViewVariational();
        });

        And("^i url search for my licence$", () -> {
            world.internalNavigation.urlSearchAndViewLicence();
        });
        And("^i url search for my application", () -> {
            world.internalNavigation.urlSearchAndViewApplication();
        });
    }
}
