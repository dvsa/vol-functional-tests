package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import apiCalls.enums.OperatorType;
import apiCalls.enums.UserType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.dvsa.testing.lib.pages.BasePage;

import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationVerifyJourney extends BasePage {
    World world;

    public ApplicationVerifyJourney(World world) {
        this.world = world;
    }

    @When("i choose to sign with verify")
    public void iChooseToSignWithVerify() {
        world.UIJourneySteps.signWithVerify();
    }

    @Then("the application should be signed with verify")
    public void theApplicationShouldBeSignedWithVerify() {
        waitForTitleToBePresent("Review and declarations");
        assertTrue(isTextPresent("Declaration signed through GOV.UK Verify", 30));
        assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("dd MMM yyyy")), 30));
    }

    @Given("i have an application in progress")
    public void iHaveAnApplicationInProgress() {
        world.createApplication.setOperatorType(OperatorType.GOODS.name());
        world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        world.APIJourneySteps.createApplication();
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("application", "Type of licence");
        world.selfServeNavigation.navigateThroughApplication();
        world.UIJourneySteps.signDeclaration();
    }
}