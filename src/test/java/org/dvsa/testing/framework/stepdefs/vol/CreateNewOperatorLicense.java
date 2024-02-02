package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.lib.url.webapp.URL;
import org.dvsa.testing.lib.url.webapp.utils.ApplicationType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateNewOperatorLicense extends BasePage {
    private final World world;
    Initialisation initialisation;
    public CreateNewOperatorLicense (World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
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

    @When("I log in as a Local Authority User")
    public void iLogInAsALocalAuthorityUser() {
        String user = world.configuration.config.getString("localAuthorityUser");
        String password = world.configuration.config.getString("defaultPassword");

        if (getDriver().getCurrentUrl().contains("dashboard")) {
            clickByLinkText("Sign out");
        }
        String externalURL = URL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();
        get(externalURL);
        waitForTextToBePresent("Password");

        {
            world.globalMethods.signIn(user, password);
        }
    }
}