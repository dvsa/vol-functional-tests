package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

public class InternalSearch extends BasePage implements En {

    public InternalSearch(World world) {

        When("^i search for and click on my licence$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.searchAndViewLicence();
        });
        And("^i search for and click on my application", () -> {
            if (isElementPresent("//select[@id='search-select']", SelectorType.XPATH)) {
                world.UIJourneySteps.searchAndViewApplication();
            } else {
                world.APIJourneySteps.createAdminUser();
                world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
                world.UIJourneySteps.searchAndViewApplication();
            }
        });
        When("^i search for and click on my case", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.searchAndViewCase();
        });
        When("^i search for my psv disc and click on my licence and discs$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.searchAndViewPSVDisc();
        });
        When("^i search for my address and click on my licence and addresses$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.searchAndViewAddress();
        });


        And("^i create admin and url search for my licence$", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.urlSearchAndViewLicence();
        });
        And("^i create admin and url search for my application", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.urlSearchAndViewApplication();
        });
        And("^i create admin and url search for my variation", () -> {
            world.APIJourneySteps.createAdminUser();
            world.UIJourneySteps.navigateToInternalAdminUserLogin(world.updateLicence.adminUserLogin, world.updateLicence.adminUserEmailAddress);
            world.UIJourneySteps.urlSearchAndViewVariational();
        });

        And("^i url search for my licence$", () -> {
            world.UIJourneySteps.urlSearchAndViewLicence();
        });
        And("^i url search for my application", () -> {
            world.UIJourneySteps.urlSearchAndViewApplication();
        });
    }
}
