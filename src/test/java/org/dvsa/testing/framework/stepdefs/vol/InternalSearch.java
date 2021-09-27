package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class InternalSearch extends BasePage implements En {

    public InternalSearch(World world) {

        When("^i search for and click on my licence$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.internalSearchJourney.searchAndViewLicence();
        });
        And("^i search for and click on my application", () -> {
            if (isElementPresent("//select[@id='search-select']", SelectorType.XPATH)) {
                world.internalSearchJourney.searchAndViewApplication();
            } else {
                world.APIJourney.createAdminUser();
                world.internalNavigation.logInAsAdmin();
                world.internalSearchJourney.searchAndViewApplication();
            }
        });
        When("^i search for and click on my case", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.internalSearchJourney.searchAndViewCase();
        });
        When("^i search for my psv disc and click on my licence and discs$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.internalSearchJourney.searchAndViewPSVDisc();
        });
        When("^i search for my address and click on my licence and addresses$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.internalSearchJourney.searchAndViewAddress();
        });

        And("^i create an admin and url search for my licence$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.internalNavigation.getLicence();
        });
        And("^i create an admin and url search for my application", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.internalNavigation.getApplication();
        });
        And("^i create an admin and url search for my variation", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.internalNavigation.getVariationApplication();
        });

        And("^i url search for my licence$", () -> {
            world.internalNavigation.getLicence();
        });
        And("^i url search for my application", () -> {
            world.internalNavigation.getApplication();
        });
    }
}
