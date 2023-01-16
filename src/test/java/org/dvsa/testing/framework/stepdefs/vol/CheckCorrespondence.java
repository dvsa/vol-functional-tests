package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckCorrespondence extends BasePage {
    private final World world;

    public CheckCorrespondence(World world) {
        this.world = world;
    }

    @And("i have logged in to self serve")
    public void iHaveLoggedInToSelfServe() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
    }

    @When("i open the documents tab")
    public void iOpenTheDocumentsTab() {
        click("//a[@href='/correspondence/']", SelectorType.XPATH);
    }

    @Then("all correspondence for the application should be displayed")
    public void allCorrespondenceForTheApplicationShouldBeDisplayed() {
        waitForElementToBePresent("//table");
        assertTrue(findElement("//table",SelectorType.XPATH,300).getText().contains(world.applicationDetails.getLicenceNumber()));
    }
}