package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SurrenderStartPage extends BasePage {
    private final World world;

    public SurrenderStartPage(World world) {this.world=world;}

    @When("i click on apply to surrender licence")
    public void iClickOnApplyToSurrenderLicence() throws IllegalBrowserException, IOException {
        world.surrenderJourney.navigateToSurrendersStartPage(false);
    }

    @Then("the correct page heading for {string} should be displayed")
    public void theCorrectPageHeadingForShouldBeDisplayed(String licenceType) {
        if(licenceType.equals("public"))
        {
            assertTrue(findElement("//h1", SelectorType.XPATH,10).getText().contains("Apply to surrender your public service vehicle operator's licence"));
        }else{
            assertTrue(findElement("//h1", SelectorType.XPATH,10).getText().contains("Apply to surrender your goods vehicle operator's licence"));
        }
    }

    @And("the correct instructions for {string} should be displayed")
    public void theCorrectInstructionsForShouldBeDisplayed(String licenceType) {
        if (licenceType.equals("public")) {
            assertTrue(isTextPresent("You will need to cancel all registered bus services."));
        }
    }

    @And("the correct licence number should be displayed")
    public void theCorrectLicenceNumberShouldBeDisplayed() {
        assertTrue(isTextPresent(world.applicationDetails.getLicenceNumber()));
    }
}