package org.dvsa.testing.framework.stepdefs.vol;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class ForgottenUserNameSteps extends BasePage {

    private final World world;

    public ForgottenUserNameSteps(World world) {
        this.world = world;
    }

    @Given("I have navigated to the Forgotten User page")
    public void iHaveNavigatedToTheForgottenUserPage() {
        clickByLinkText("Sign out");
        world.selfServeNavigation.navigateToLoginPage();
        clickByLinkText("Forgotten your username?");
    }

    @Then("I complete the forgotten username process")
    public void iCompleteTheForgottenUsernameProcess() {
        waitAndEnterText("fields[licenceNumber]", SelectorType.ID, world.applicationDetails.getLicenceNumber());
        waitAndEnterText("fields[emailAddress]", SelectorType.ID, world.createApplication.getTransportManagerEmailAddress());
        UniversalActions.clickSubmit();
        world.selfServeNavigation.navigateToLoginPage();
    }


    @Then("the username is now displayed on the sign in page")
    public void theUsernameIsNowDisplayedOnTheSignInPage() {
        String userName = world.configuration.getForgottenUsername();
        world.globalMethods.signIn(userName, world.configuration.config.getString("adminPassword"));
        assert(isTextPresent(userName));
    }
}
