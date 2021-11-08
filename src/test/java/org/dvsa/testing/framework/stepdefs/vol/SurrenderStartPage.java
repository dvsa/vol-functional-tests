package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static junit.framework.TestCase.assertTrue;

public class SurrenderStartPage extends BasePage implements En {
    private final World world;

    public SurrenderStartPage(World world) {this.world=world;}

    @When("i click on apply to surrender licence")
    public void iClickOnApplyToSurrenderLicence() {
        world.surrenderJourney.navigateToSurrendersStartPage();
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
        boolean isTrue = findElement("//h3", SelectorType.XPATH,10).getText().contains(world.applicationDetails.getLicenceNumber());
        assertTrue(isTrue);
    }
}
