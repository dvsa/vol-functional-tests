package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import cucumber.api.java8.En;
import enums.UserRoles;
import org.apache.commons.io.FileUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.support.Color;
import scanner.AXEScanner;

import java.io.File;
import java.util.Locale;


public class ManageUsersPage extends BasePage implements En {
    AXEScanner scanner;

    public ManageUsersPage(World world) {
        Given("^i have an admin account to add users$", () -> {
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
        });
        When("^i navigate to the manage users page$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            waitAndClick("//*[contains(text(),'Manage users')]", SelectorType.XPATH);
            Assert.assertEquals("Manage users", getText("h1", SelectorType.CSS));
        });
        And("^i scan for accessibility violations$", () -> {
            this.scanner = new AXEScanner();
            this.scanner.scan();
        });
        Then("^no issues should be present on the page$", () -> {
            Assert.assertEquals(0, scanner.axeFindings().length());
            FileUtils.writeStringToFile(new File("Findings.txt"), scanner.axeFindings());
        });
        Then("^name of button should be 'Add a user'$", () -> {
            Assert.assertEquals("Add a user", getAttribute("action", SelectorType.NAME, "value"));
        });
        Then("^colour of the 'Add a user' button should be green$", () -> {
            String buttonColour = Color.fromString(getCssValue("action", SelectorType.NAME, "background-color")).asHex();
            Assert.assertEquals("#00823B", buttonColour);
        });
        Then("^remove button column should be named 'Action'$", () -> {
            findElements(".//tr/th[4]",SelectorType.XPATH).forEach(
                    title -> Assert.assertTrue(title.getText().contains("Action")));
        });
        When("^i add a user$", () -> {
            FakeValuesService fakeValuesService = new FakeValuesService(
                    new Locale("en-GB"), new RandomService());

            Faker faker = new Faker();

            world.UIJourneySteps.addUser(faker.name().username(), fakeValuesService.bothify("????##@dvsa.org"),
                    faker.name().firstName(), faker.name().lastName());
        });
        Then("^user text should displaying current users$", () -> {
            Assert.assertEquals("2 current users", getText("h3", SelectorType.CSS));
        });
    }
}