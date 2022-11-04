package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateNewOperatorLicense extends BasePage {
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
        assertTrue(isTextPresent(world.DataGenerator.getOperatorUserEmail()));
    }

    @And("I am on the registration page")
    public void iAmOnTheRegistrationPage() {
        world.selfServeNavigation.navigateToLoginPage();
        world.selfServeNavigation.navigateToCreateAnAccount();
    }
}