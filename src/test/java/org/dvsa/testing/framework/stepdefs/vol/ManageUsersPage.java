package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import apiCalls.Utils.generic.Utils;
import apiCalls.enums.UserType;
import cucumber.api.java8.En;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.support.Color;
import scanner.AXEScanner;
import scanner.ReportGenerator;

public class ManageUsersPage extends BasePage implements En {
    AXEScanner scanner = new AXEScanner();
    ReportGenerator reportGenerator = new ReportGenerator();
    private static final Logger LOGGER = LogManager.getLogger(ManageUsersPage.class);

    public ManageUsersPage(World world) {
        Given("^i have an admin account to add users$", () -> {
            world.APIJourneySteps.registerAndGetUserDetails(UserType.EXTERNAL.asString());
        });
        When("^i navigate to the manage users page$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            waitAndClick("//*[contains(text(),'Manage users')]", SelectorType.XPATH);
            Assert.assertEquals("Manage users", getText("h1", SelectorType.CSS));
        });
        And("^i scan for accessibility violations$", () -> {
            scanner.scan();
        });
        Then("^no issues should be present on the page$", () -> {
            if (scanner.getTotalViolationsCount() != 0) {
                LOGGER.info("ERROR: Violation found");
            } else {
                LOGGER.info("No violation found");
            }
            reportGenerator.urlScannedReportSection(Browser.navigate().getCurrentUrl());
            reportGenerator.violationsReportSectionHTML(Browser.navigate().getCurrentUrl(), scanner);
            reportGenerator.createReport(scanner);
        });
        Then("^name of button should be 'Add a user'$", () -> {
            Assert.assertEquals("Add a user", getAttribute("action", SelectorType.NAME, "data-label"));
        });
        Then("^colour of the 'Add a user' button should be green$", () -> {
            String buttonColour = Color.fromString(getCssValue("action", SelectorType.NAME, "background-color")).asHex();
            Assert.assertEquals("#00823b", buttonColour);
        });
        Then("^remove button column should be named 'Action'$", () -> {
            findElements(".//tr/th[4]", SelectorType.XPATH).forEach(
                    title -> Assert.assertTrue(title.getText().contains("Action")));
        });
        When("^i add a user$", () -> {
            FakerUtils faker = new FakerUtils();
            String foreName = faker.generateFirstName();
            String familyName = faker.generateLastName();
            String userName = String.format("%s.%s%s", foreName, familyName, Int.random(1000, 9999));
            world.UIJourneySteps.addUser(userName, foreName, familyName, userName.concat("@dvsa.org"));
        });
        Then("^user text should displaying current users$", () -> {
            Assert.assertEquals("2 Current users", getText("h2", SelectorType.CSS));
        });
    }
}