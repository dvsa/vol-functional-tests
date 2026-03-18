package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.support.Color;
import org.dvsa.testing.framework.axe.AXEScanner;
import org.dvsa.testing.framework.jsoup.SpiderCrawler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.fail;


public class AccessibilitySteps extends BasePage {
    private static final Logger LOGGER = LogManager.getLogger(AccessibilitySteps.class);
    private final World world;

    public AccessibilitySteps(World world) {
        this.world = world;
    }

    @Then("the colour of the {string} section should be blue")
    public void theColourOfTheBusinessTypeSectionShouldBeBlue(String sectionTitle) {
        String buttonColour = Color.fromString(
                findElement(String.format("//*[contains(text(),'%s')]", sectionTitle), SelectorType.XPATH)
                        .getCssValue("color")).asHex();
        assertEquals("#1d70b8", buttonColour);
    }

    @Then("the colour of the {string} section should be grey")
    public void theColourOfTheDirectorsSectionShouldBeGrey(String sectionTitle) {
        Color buttonColour = Color.fromString(findElement(String.format("//*[contains(text(),'%s')]", sectionTitle), SelectorType.XPATH).getCssValue("color"));
        assertEquals(Color.fromString("rgba(80, 90, 95, 1)"), buttonColour);

    }
    @Then("the colour of the {string} section should be purple")
    public void theColourOfTheBusinessTypeSectionShouldBePurple(String sectionTitle) {
        String buttonColour = Color.fromString(
                findElement(String.format("//*[contains(text(),'%s')]", sectionTitle), SelectorType.XPATH)
                        .getCssValue("color")).asHex();
        assertEquals("#4c2c92", buttonColour);
    }

    @Then("colour of the {string} button should be green")
    public void colourOfTheAddAUserButtonShouldBeGreen(String buttonName) {
        String buttonColour = Color.fromString(
                findElement(String.format("//*[contains(text(),'%s')]", buttonName), SelectorType.XPATH)
                        .getCssValue("background-color")).asHex();
        assertEquals("#00703c", buttonColour);
    }

    @BeforeAll
    @When("i scan for accessibility violations")
    @Step("Scan page for accessibility violations")
    public static void iScanForAccessibilityViolations() throws IllegalBrowserException {
        AXEScanner scanner = new AXEScanner();
        scanner.scan(Browser.getDriver());
        SpiderCrawler.crawler(1, getCurrentUrl(), new HashSet<>(), Browser.getDriver());
    }

    @AfterAll
    @Then("no issues should be present on the page")
    @Step("Verify no accessibility issues")
    public void noIssuesShouldBePresentOnThePage() {
        AXEScanner scanner = new AXEScanner();
        scanner.generateFinalReport();
    }
}

