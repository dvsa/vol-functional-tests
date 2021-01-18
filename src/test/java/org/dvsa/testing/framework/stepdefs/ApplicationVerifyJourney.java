package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import apiCalls.enums.OperatorType;
import apiCalls.enums.UserType;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;

import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationVerifyJourney extends BasePage implements En {
    public ApplicationVerifyJourney(World world) {
        Given("^i have an application in progress$", () -> {
            world.createApplication.setOperatorType(OperatorType.PUBLIC.name());
            world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
            world.APIJourneySteps.createApplication();
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("application", "Review and declarations");
            world.UIJourneySteps.signDeclaration();
        });
        When("^i choose to sign with verify with \"([^\"]*)\"$", (String arg0) -> {
            world.UIJourneySteps.signWithVerify();
        });
        Then("^the application should be signed with verify$", () -> {
            waitForTitleToBePresent("Review and declarations");
            assertTrue(isTextPresent("Declaration signed through GOV.UK Verify",30));
            assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s", getCurrentDate("dd MMM yyyy")),30));
        });
    }
}