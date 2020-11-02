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
            world.selfServeNavigation.navigateToLogin( world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
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
        And("^i choose to sign with print and sign$", () -> {
            click("//*[contains(text(),'Print')]", SelectorType.XPATH);
            click("form-actions[submit]", SelectorType.ID);
        });
    }
}