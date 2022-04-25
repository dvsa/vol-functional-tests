package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.database.exception.UnsupportedDatabaseDriverException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.sql.SQLException;

public class InternalSearch extends BasePage implements En {
    private final World world;

    public InternalSearch (World world) {this.world =world;}

    @When("i search for and click on my licence")
    public void iSearchForAndClickOnMyLicence() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalSearchJourney.searchAndViewLicence();
    }

    @When("i search for and click on my application")
    public void iSearchForAndClickOnMyApplication() {
        if (isElementPresent("//select[@id='search-select']", SelectorType.XPATH)) {
            world.internalSearchJourney.searchAndViewApplication();
        } else {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.internalSearchJourney.searchAndViewApplication();
        }
    }

    @When("i search for and click on my case")
    public void iSearchForAndClickOnMyCase() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalSearchJourney.searchAndViewCase();
    }

    @When("i search for my psv disc and click on my licence and discs")
    public void iSearchForMyPsvDiscAndClickOnMyLicenceAndDiscs() throws SQLException, UnsupportedDatabaseDriverException {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalSearchJourney.searchAndViewPSVDisc();
    }

    @When("i search for my address and click on my licence and addresses")
    public void iSearchForMyAddressAndClickOnMyLicenceAndAddresses() {
        world.APIJourney.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        world.internalSearchJourney.searchAndViewAddress();
    }

    @And("i create an admin and url search for my licence")
    public void iCreateAnAdminAndUrlSearchForMyLicence() {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
    }

    @And("i create an admin and url search for my application")
    public void iCreateAnAdminAndUrlSearchForMyApplication() {
        world.internalNavigation.navigateToPage("application", SelfServeSection.VIEW);
    }

    @And("i create an admin and url search for my variation")
    public void iCreateAnAdminAndUrlSearchForMyVariation() {
        world.internalNavigation.navigateToPage("variation", SelfServeSection.VIEW);
    }

    @And("i url search for my licence")
    public void iUrlSearchForMyLicence() {
        world.internalNavigation.getLicence();
    }

    @And("i url search for my application")
    public void iUrlSearchForMyApplication() {
        world.internalNavigation.getApplication();
    }
}
