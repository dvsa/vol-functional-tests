package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;


public class InternalSearch extends BasePage {
    private final World world;
    public InternalSearch(World world) {
        this.world = world;
    }
    @When("i search for and click on my licence")
    public void iSearchForAndClickOnMyLicence() throws HttpException {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalSearchJourney.searchAndViewLicence();
    }
    @When("i search for and click on my application")
    public void iSearchForAndClickOnMyApplication() throws HttpException {
        if (isElementPresent("//select[@id='search-select']", SelectorType.XPATH)) {
            world.internalSearchJourney.searchAndViewApplication();
        } else {
            world.APIJourney.createAdminUser();
            world.internalNavigation.logInAsAdmin();
            world.internalSearchJourney.searchAndViewApplication();
        }
    }
    @When("i search for and click on my case")
    public void iSearchForAndClickOnMyCase() throws HttpException {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalSearchJourney.searchAndViewCase();
    }
    @When("i search for my psv disc and click on my licence and discs")
    public void iSearchForMyPsvDiscAndClickOnMyLicenceAndDiscs() throws HttpException {
        world.APIJourney.createAdminUser();
        world.internalNavigation.logInAsAdmin();
        world.internalSearchJourney.searchAndViewPSVDisc();
    }
    @When("i search for my address and click on my licence and addresses")
    public void iSearchForMyAddressAndClickOnMyLicenceAndAddresses() throws HttpException {
        world.APIJourney.createAdminUser();
        world.internalNavigation.navigateToLogin(world.updateLicence.getInternalUserLogin(), world.updateLicence.getInternalUserEmailAddress());
        world.internalSearchJourney.searchAndViewAddress();
    }
    @And("i create an admin and url search for my licence")
    public void iCreateAnAdminAndUrlSearchForMyLicence() throws HttpException {
        world.internalNavigation.navigateToPage("licence", SelfServeSection.VIEW);
    }
    @And("i create an admin and url search for my application")
    public void iCreateAnAdminAndUrlSearchForMyApplication() throws HttpException {
        world.internalNavigation.navigateToPage("application", SelfServeSection.VIEW);
    }
    @And("i create an admin and url search for my variation")
    public void iCreateAnAdminAndUrlSearchForMyVariation() throws HttpException {
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