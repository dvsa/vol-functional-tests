package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.axe.AXEScanner;
import org.dvsa.testing.framework.axe.AXEScanner.ViolationEntry;
import org.dvsa.testing.framework.jsoup.SpiderCrawler;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class AccessibilitySteps extends BasePage {
    private static final Logger LOGGER = LogManager.getLogger(AccessibilitySteps.class);
    private final World world;

    private List<ViolationEntry> scenarioViolations = new ArrayList<>();

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
        assertEquals("#0f7a52", buttonColour);
    }


    @When("i scan for accessibility violations")
    @Step("Scan page for accessibility violations")
    public void iScanForAccessibilityViolations() throws IllegalBrowserException {
        String currentUrl = Browser.navigate().getCurrentUrl();
        LOGGER.info("Scanning page for accessibility violations: {}", currentUrl);

        int beforeSize = AXEScanner.getAllViolations().size();
        WebDriver driver = Browser.navigate();
        AXEScanner.scan(driver);

        LOGGER.info("Starting spider crawl from: {}", currentUrl);
        SpiderCrawler.crawler(1, currentUrl, new HashSet<>(), driver);

        int afterSize = AXEScanner.getAllViolations().size();
        int newViolations = afterSize - beforeSize;
        if (newViolations > 0) {
            List<ViolationEntry> allViols = AXEScanner.getAllViolations();
            scenarioViolations.addAll(allViols.subList(beforeSize, afterSize));
        }

        LOGGER.info("Scan + crawl complete - New violations found: {}", newViolations);
    }


    @Then("no issues should be present on the page")
    @Step("Verify no accessibility issues")
    public void noIssuesShouldBePresentOnThePage() {
        int totalViolations = scenarioViolations.size();
        long criticalViolations = scenarioViolations.stream()
                .filter(v -> "critical".equalsIgnoreCase(v.rule().getImpact()))
                .count();

        if (totalViolations > 0) {
            StringBuilder summary = new StringBuilder();
            for (ViolationEntry entry : scenarioViolations) {
                summary.append(String.format("  [%s] %s - %s on %s%n",
                        entry.rule().getImpact(),
                        entry.rule().getId(),
                        entry.rule().getDescription(),
                        entry.pageUrl()));
            }
            Allure.addAttachment("Accessibility Violations", "text/plain", summary.toString());
        }

        if (totalViolations == 0) {
            LOGGER.info("✓ No accessibility violations found");
        } else if (criticalViolations == 0) {
            LOGGER.warn("Found {} accessibility violation(s) but none are CRITICAL - Test PASSES",
                    totalViolations);
        } else {
            LOGGER.error("✗ Found {} CRITICAL accessibility violation(s) - Test FAILS", criticalViolations);
            String violationDetails = scenarioViolations.stream()
                    .filter(v -> "critical".equalsIgnoreCase(v.rule().getImpact()))
                    .map(v -> String.format("[%s] %s: %s", v.rule().getImpact(), v.rule().getId(), v.rule().getDescription()))
                    .reduce("", (a, b) -> a + "\n" + b);
            fail(String.format("Found %d CRITICAL violation(s):\n%s", criticalViolations, violationDetails));
        }

        scenarioViolations.clear();
    }
}
