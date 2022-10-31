package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import com.mailslurp.clients.ApiException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class GovSignIn extends BasePage {
    private final World world;

    public GovSignIn(World world) {this.world = world;}

    @Given("I can navigate to gov sign in")
    public void iCanNavigateToGovSignIn() {
        world.govSignInJourney.navigateToGovUkSignIn();
    }

    @And("I create an account")
    public void iCreateAnAccount() throws Exception {
        world.govSignInJourney.createGovAccount();
    }
}
