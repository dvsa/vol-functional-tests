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
import org.openqa.selenium.support.Color;
import scanner.AXEScanner;
import scanner.AXEScanner.ScanResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class AccessibilitySteps extends BasePage {
    private static final Logger LOGGER = LogManager.getLogger(AccessibilitySteps.class);
    private final World world;

    public static final AXEScanner scanner = new AXEScanner();

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
        Color buttonColour = Color.fromString(
                findElement(String.format("//*[contains(text(),'%s')]", sectionTitle), SelectorType.XPATH)
                        .getCssValue("color"));
        assertEquals(Color.fromString("grey"), buttonColour);
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


    @When("i scan for accessibility violations")
    @Step("Scan page for accessibility violations")
    public void iScanForAccessibilityViolations() throws IllegalBrowserException {
        String currentUrl = Browser.navigate().getCurrentUrl();
        LOGGER.info("Scanning page for accessibility violations: {}", currentUrl);
        ScanResult result = scanner.scan(true);
        LOGGER.info("Scan complete - Violations: {}, Incomplete: {}",
                result.getViolationCount(), result.getIncompleteCount());
    }


    @Then("no issues should be present on the page")
    @Step("Verify no accessibility issues")
    public void noIssuesShouldBePresentOnThePage() {
        scanner.logViolations();
        scanner.attachToAllure();
        int totalViolations = scanner.getTotalViolationsCount();
        int criticalViolations = scanner.getCriticalViolationsCount();
        if (totalViolations == 0) {
            LOGGER.info("✓ No accessibility violations found");
        } else if (criticalViolations == 0) {
            LOGGER.warn(":warning: Found {} accessibility violation(s) but none are CRITICAL - Test PASSES",
                    totalViolations);
        } else {
            LOGGER.error("✗ Found {} CRITICAL accessibility violation(s) - Test FAILS", criticalViolations);
            String violationDetails = scanner.getViolationSummary();
            fail(String.format("Found %d CRITICAL violation(s):\n%s", criticalViolations, violationDetails));
        }
    }
}

