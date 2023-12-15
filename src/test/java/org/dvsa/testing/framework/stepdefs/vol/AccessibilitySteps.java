package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Journeys.licence.BusinessDetailsJourney;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.external.pages.baseClasses.BasePermitPage;
import org.openqa.selenium.support.Color;
import scanner.AXEScanner;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccessibilitySteps extends BasePage {
    private final World world;

    public AccessibilitySteps(World world) {this.world = world;}

    @Then("the colour of the {string} section should be blue")
    public void theColourOfTheBusinessTypeSectionShouldBeBlue(String sectionTitle) {
        String buttonColour = Color.fromString(findElement(String.format("//*[contains(text(),'%s')]", sectionTitle), SelectorType.XPATH).getCssValue("color")).asHex();
        assertEquals("#1d70b8", buttonColour);
    }

    @Then("the colour of the {string} section should be grey")
    public void theColourOfTheDirectorsSectionShouldBeGrey(String sectionTitle) {
        String buttonColour = Color.fromString(findElement(String.format("//*[contains(text(),'%s')]", sectionTitle), SelectorType.XPATH).getCssValue("color")).asHex();
        assertEquals("#505a5f", buttonColour);
    }

    @Then("the colour of the {string} section should be purple")
    public void theColourOfTheBusinessTypeSectionShouldBePurple(String sectionTitle) {
        String buttonColour = Color.fromString(findElement(String.format("//*[contains(text(),'%s')]", sectionTitle), SelectorType.XPATH).getCssValue("color")).asHex();
        assertEquals("#4c2c92", buttonColour);
    }

}
