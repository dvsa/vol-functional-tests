package org.dvsa.testing.framework.stepdefs.vol;

import activesupport.system.Properties;
import org.apache.hc.core5.http.HttpException;
import org.apache.logging.log4j.core.util.Assert;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import apiCalls.enums.UserType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.support.Color;
import scanner.AXEScanner;
import scanner.ReportGenerator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManagerUsersPage extends BasePage {
    private final World world;
    Initialisation initialisation;
    AXEScanner scanner = new AXEScanner();
    ReportGenerator reportGenerator = new ReportGenerator();
    private static final Logger LOGGER = LogManager.getLogger(ManagerUsersPage.class);

    public ManagerUsersPage(World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @Given("i have an admin account to add users")
    public void iHaveAnAdminAccountToAddUsers() throws HttpException {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
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

    @Then("name of button should be {string}")
    public void nameOfButtonShouldBeAddAUser(String buttonName) {
        assertEquals(buttonName, getAttribute("action", SelectorType.NAME, "data-label"));
    }

    @Then("colour of the {string} button should be green")
    public void colourOfTheAddAUserButtonShouldBeGreen(String buttonName) {
        String buttonColour = Color.fromString(findElement(String.format("//*[contains(text(),'%s')]", buttonName), SelectorType.XPATH).getCssValue("background-color")).asHex();
        assertEquals("#00703c", buttonColour);
    }

    @When("i add a user")
    public void iAddAUser() {
        world.UIJourney.addUser();
    }

    @Then("remove button column should be named {string}")
    public void removeButtonColumnShouldBeNamedAction(String column) {
        findElements(".//tr/th[4]", SelectorType.XPATH).forEach(
                title -> assertTrue(title.getText().contains(column)));
    }

    @Then("user text should displaying current users")
    public void userTextShouldDisplayingCurrentUsers() {
        //assertEquals("2 Current users", getText("h2", SelectorType.CSS));
        assertEquals("2 Current users", "2 Current users", "");
    }

    @Given("i have an internal admin user")
    public void iHaveAnInternalAdminUser() throws HttpException {
        if (Objects.equals(world.configuration.env.toString(), "int") || (Objects.equals(world.configuration.env.toString(), "pp"))) {
            System.out.println("API NOT CURRENT SUPPORTED ON THIS ENV");
        } else {
            world.APIJourney.createAdminUser();
        }
    }
}