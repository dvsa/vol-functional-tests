package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import apiCalls.enums.UserType;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import org.openqa.selenium.support.Color;
import scanner.AXEScanner;
import scanner.ReportGenerator;

import java.io.IOException;
import java.net.URISyntaxException;

public class ManagerUsersPage extends BasePage implements En {
    private final World world;

    AXEScanner scanner = new AXEScanner();
    ReportGenerator reportGenerator = new ReportGenerator();
    private static final Logger LOGGER = LogManager.getLogger(ManagerUsersPage.class);

    public ManagerUsersPage(World world) {this.world = world;}

    @Given("i have an admin account to add users")
    public void iHaveAnAdminAccountToAddUsers() {
        world.APIJourney.registerAndGetUserDetails(UserType.EXTERNAL.asString());
    }

    @And("i navigate to the manage users page")
    public void iNavigateToTheManageUsersPage() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        waitAndClick("//*[contains(text(),'Manage users')]", SelectorType.XPATH);
        Assert.assertEquals("Manage users", getText("h1", SelectorType.CSS));
    }

    @When("i scan for accessibility violations")
    public void iScanForAccessibilityViolations() throws IllegalBrowserException, IOException {
        scanner.scan();
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
        Assert.assertEquals(buttonName, getAttribute("action", SelectorType.NAME, "data-label"));
    }

    @Then("colour of the {string} button should be green")
    public void colourOfTheAddAUserButtonShouldBeGreen(String buttonName) {
        String buttonColour = Color.fromString(findElement(String.format("//*[contains(text(),'%s')]",buttonName), SelectorType.XPATH).getCssValue("background-color")).asHex();
        Assert.assertEquals("#00823b", buttonColour);
    }

    @When("i add a user")
    public void iAddAUser() {
        world.UIJourney.addUser();
    }

    @Then("remove button column should be named {string}")
    public void removeButtonColumnShouldBeNamedAction(String column) {
        findElements(".//tr/th[4]", SelectorType.XPATH).forEach(
                title -> Assert.assertTrue(title.getText().contains(column)));
    }

    @Then("user text should displaying current users")
    public void userTextShouldDisplayingCurrentUsers() {
        Assert.assertEquals("2 Current users", getText("h2", SelectorType.CSS));
    }
}