package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.support.Color;
import scanner.AXEScanner;
import scanner.ReportGenerator;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccessibilitySteps extends BasePage {
    private static final Logger LOGGER = LogManager.getLogger(AccessibilitySteps.class);
    private final World world;

    public static final AXEScanner scanner = new AXEScanner();

    ReportGenerator reportGenerator = new ReportGenerator();

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

    @Then("colour of the {string} button should be green")
    public void colourOfTheAddAUserButtonShouldBeGreen(String buttonName) {
        String buttonColour = Color.fromString(findElement(String.format("//*[contains(text(),'%s')]", buttonName), SelectorType.XPATH).getCssValue("background-color")).asHex();
        assertEquals("#00703c", buttonColour);
    }

    @When("i scan for accessibility violations")
    public void iScanForAccessibilityViolations() throws IllegalBrowserException, IOException {
        scanner.scan(false);
    }

    @Then("no issues should be present on the page")
    public void noIssuesShouldBePresentOnThePage() throws IOException, URISyntaxException {
        if (scanner.getTotalViolationsCount() != 0) {
            LOGGER.info("ERROR: Violation found");
        } else {
            LOGGER.info("No violation found");
        }
        reportGenerator.urlScannedReportSection(Browser.navigate().getCurrentUrl());
        reportGenerator.violationsReportSectionHTML(Browser.navigate().getCurrentUrl(), scanner);
        reportGenerator.createReport(scanner);
    }

}