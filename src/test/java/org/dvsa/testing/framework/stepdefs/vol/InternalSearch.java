package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
<<<<<<< HEAD
import io.cucumber.java8.En;;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
=======
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
>>>>>>> d8085593ab4c7bbad63e837e7c025193e92cdcf3

public class InternalSearch extends BasePage implements En {

    public InternalSearch(World world) {

        When("^i search for and click on my licence$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalSearchJourney.searchAndViewLicence();
        });
        And("^i search for and click on my application", () -> {
            if (isElementPresent("//select[@id='search-select']", SelectorType.XPATH)) {
                world.internalSearchJourney.searchAndViewApplication();
            } else {
                world.APIJourney.createAdminUser();
                world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
                world.internalSearchJourney.searchAndViewApplication();
            }
        });
        When("^i search for and click on my case", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalSearchJourney.searchAndViewCase();
        });
        When("^i search for my psv disc and click on my licence and discs$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalSearchJourney.searchAndViewPSVDisc();
        });
        When("^i search for my address and click on my licence and addresses$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalSearchJourney.searchAndViewAddress();
        });

        And("^i create an admin and url search for my licence$", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalNavigation.urlSearchAndViewLicence();
        });
        And("^i create an admin and url search for my application", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
            world.internalNavigation.urlSearchAndViewApplication();
        });
        And("^i create an admin and url search for my variation", () -> {
            world.APIJourney.createAdminUser();
            world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
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
