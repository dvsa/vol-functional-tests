package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java8.En;

import org.dvsa.testing.framework.pageObjects.BasePage;


public class CreateNewOperatorLicense extends BasePage implements En {
    private final World world;

    public CreateNewOperatorLicense (World world) {
        this.world = world;
    }

    @Given("an Operator with no licence")
    public void anOperatorWithNoLicence() {
        world.UIJourney.addNewOperator(null, false);
    }

    @Then("I should be able to Create account with my existing licence")
    public void iShouldBeAbleToCreateAccountWithMyExistingLicence() {
        world.UIJourney.addNewOperator(world.applicationDetails.getLicenceNumber(), true);
    }

    @Then("I should be able to register an account")
    public void iShouldBeAbleToRegisterAnAccount() {
        isTextPresent("Check your email");
    }

    @And("I am on the registration page")
    public void iAmOnTheRegistrationPage() {
        world.selfServeNavigation.navigateToLoginPage();
        world.selfServeNavigation.navigateToCreateAnAccount();
    }
}
