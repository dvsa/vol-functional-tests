package org.dvsa.testing.framework.stepdefs.vol;


import io.cucumber.java.en.Given;
import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.OperatorType;
import apiCalls.enums.UserType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
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
        refreshPageWithJavascript();
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.TYPE_OF_LICENCE);
        world.selfServeNavigation.navigateThroughApplication();
        world.UIJourney.signDeclaration();
    }
    @When("i choose to sign with verify")
    public void iChooseToSignWithVerify() {
        world.UIJourney.signWithVerify();
    }
}