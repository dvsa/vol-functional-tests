package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import enums.UserRoles;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import static org.dvsa.testing.framework.Utils.Generic.GenericUtils.getCurrentDate;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationVerifyJourney extends BasePage implements En {
    public ApplicationVerifyJourney(World world) {
        Given("^i have an application in progress$", () -> {
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.APIJourneySteps.createApplication();
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            world.UIJourneySteps.navigateToReviewDeclarationsPage("application");
            world.UIJourneySteps.signDeclaration();
        });
        When("^i choose to sign with verify with \"([^\"]*)\"$", (String arg0) -> {
            world.UIJourneySteps.signWithVerify();
        });
        Then("^the application should be signed with verify$", () -> {
            waitForTextToBePresent("Review and declarations");
            assertTrue(isTextPresent("Declaration signed through GOV.UK Verify", 10));
            assertTrue(isTextPresent(String.format("Signed by Veena Pavlov on %s",getCurrentDate("dd MMM yyyy")),20));
        });
    }
}