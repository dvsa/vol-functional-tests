package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
 import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import static junit.framework.TestCase.assertTrue;

public class SurrendersStartPage extends BasePage implements En {
    public SurrendersStartPage(World world) {
        Then("^the correct page heading for \"([^\"]*)\" should be displayed$", (String licenceType) -> {
            if(licenceType.equals("public"))
            {
                assertTrue(findElement("//h1", SelectorType.XPATH,10).getText().contains("Apply to surrender your public service vehicle operator's licence"));
            }else{
                assertTrue(findElement("//h1", SelectorType.XPATH,10).getText().contains("Apply to surrender your goods vehicle operator's licence"));
            }
        });
        And("^the correct instructions for \"([^\"]*)\" should be displayed$", (String licenceType) -> {
            if(licenceType.equals("public"))
                assertTrue(isTextPresent("You will need to cancel all registered bus services.",30));
        });
        And("^the correct licence number should be displayed$", () -> {
            boolean isTrue = findElement("//h3", SelectorType.XPATH,10).getText().contains(world.createLicence.getLicenceNumber());
            assertTrue(isTrue);
        });
        When("^i click on the surrenders tab$", () -> {

        });
    }
}