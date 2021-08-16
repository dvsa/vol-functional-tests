package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import io.cucumber.java8.En;;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static junit.framework.TestCase.assertTrue;

public class SurrendersStartPage extends BasePage implements En {
    public SurrendersStartPage(World world) {
        When("^i click on apply to surrender licence$", () -> {
            world.surrenderJourney.navigateToSurrendersStartPage();
        });
        Then("^the correct page heading for \"([^\"]*)\" should be displayed$", (String licenceType) -> {
            if(licenceType.equals("public"))
            {
                assertTrue(findElement("//h1", SelectorType.XPATH,10).getText().contains("Apply to surrender your public service vehicle operator's licence"));
            }else{
                assertTrue(findElement("//h1", SelectorType.XPATH,10).getText().contains("Apply to surrender your goods vehicle operator's licence"));
            }
        });
        And("^the correct instructions for \"([^\"]*)\" should be displayed$", (String licenceType) -> {
            if(licenceType.equals("public")) {
                assertTrue(isTextPresent("You will need to cancel all registered bus services."));
            }
        });
        And("^the correct licence number should be displayed$", () -> {
            boolean isTrue = findElement("//h3", SelectorType.XPATH,10).getText().contains(world.applicationDetails.getLicenceNumber());
            assertTrue(isTrue);
        });
    }
}