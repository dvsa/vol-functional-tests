package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.aws.s3.SecretsManager;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.lib.url.webapp.webAppURL;
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
        world.selfServeUIJourney.addNewOperator();
    }

    @Then("I should be able to Create account with my existing licence")
    public void iShouldBeAbleToCreateAccountWithMyExistingLicence() {
        world.selfServeUIJourney.addNewOperator();
    }

    @Then("I should be able to register an account")
    public void iShouldBeAbleToRegisterAnAccount() {
        assertTrue(isTextPresent(world.DataGenerator.getOperatorUserEmail()));
    }

    @Given("I am on the registration page")
    public void iAmOnTheRegistrationPage() {
        world.selfServeNavigation.navigateToLoginPage();
        world.selfServeNavigation.navigateToCreateAnAccount();
    }

    @When("I log in as a Local Authority User")
    public void iLogInAsALocalAuthorityUser() {
        String user = SecretsManager.getSecretValue("localAuthorityUser");
        String password = SecretsManager.getSecretValue("defaultPassword");

        if (getDriver().getCurrentUrl().contains("dashboard")) {
            clickByLinkText("Sign out");
        }
        String externalURL = webAppURL.build(ApplicationType.EXTERNAL, world.configuration.env, "auth/login").toString();
        get(externalURL);
        waitForTextToBePresent("Password");
        {
            world.globalMethods.signIn(user, password);
        }
    }

    @And("a Consultant creating accounts on behalf of the operator")
    public void aConsultantCreatingAccountsOnBehalfOfTheOperator() {
        world.selfServeUIJourney.consultantCreatesAccounts();
    }

    @And("I have an existing application or licence")
    public void iHaveAnExistingApplicationOrLicence() {
        world.selfServeUIJourney.existingAppOrLicence();
    }

    @Then("I should be advised that I cannot create a new account")
    public void iShouldBeAdvisedThatICannotCreateANewAccount() {
        assertTrue(isTextPresent("Sorry, you cannot create a new account if you have an existing licence or application"));
    }

    @Then("accounts should be registered for both Operator and Consultant")
    public void accountsShouldBeRegisteredForBothOperatorAndConsultant() {
        assertTrue(isTextPresent(world.DataGenerator.getOperatorUserEmail()));
        assertTrue(isTextPresent(world.DataGenerator.getConsultantUserEmail()));
    }
}
