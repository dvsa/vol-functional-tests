package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.OperatorType;
import apiCalls.enums.UserType;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dvsa.testing.lib.pages.BasePage;

import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationVerifyJourney extends BasePage {
    World world;

    public ApplicationVerifyJourney(World world){
        this.world = world;
    }

    @Given("i have an application in progress")
    public void iHaveAnApplicationInProgress() {
        world.createApplication.setOperatorType(OperatorType.PUBLIC.name());
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.APIJourney.createApplication();
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("application", "Type of licence");
        world.selfServeNavigation.navigateThroughApplication();
        world.UIJourney.signDeclaration();
    }

    @When("i choose to sign with verify")
    public void iChooseToSignWithVerify() {
        world.UIJourney.signWithVerify();
    }

    @Then("the application should be signed with verify")
    public void theApplicationShouldBeSignedWithVerify() {
        waitForTitleToBePresent("Review and declarations");
        assertTrue(isTextPresent("Declaration signed through GOV.UK Verify", 30));
        assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("dd MMM yyyy")), 30));
    }
}