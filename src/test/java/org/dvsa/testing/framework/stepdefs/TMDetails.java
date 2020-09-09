package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.driver.Browser;
import io.cucumber.datatable.DataTable;
import cucumber.api.java8.En;
import enums.UserRoles;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.By;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TMDetails extends BasePage implements En {

    public TMDetails(World world) {
        Given("^I have a new application$", () -> {
            world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
            world.APIJourneySteps.createPartialApplication();
        });
        And("^the \"([^\"]*)\" button should not be displayed$", (String button) -> {
            assertTrue(Browser.navigate().findElements(By.xpath("//button")).stream().noneMatch(x -> x.getText().contains(button)));
        });
        When("^I select yes to all radio buttons$", () -> {
            world.genericUtils.findSelectAllRadioButtonsByValue("Y");
        });
        When("^I click on the \"([^\"]*)\" button$", (String button) -> {
            world.genericUtils.findSelectAllRadioButtonsByValue("Y");
            click(String.format("//*[@data-label=\"%s\"]", button), SelectorType.XPATH);
        });
        Then("^I should be taken to the \"([^\"]*)\" page$", (String page) -> {
            assertTrue(Browser.navigate().getCurrentUrl().contains(page));
        });
        And("^page title \"([^\"]*)\" should be displayed on page$", (String arg0) -> {
            assertTrue(isTextPresent(arg0,30));
            clickByLinkText("Back");
        });
        When("^the users attempts to save without entering any data$", () -> {
            click("form-actions[submit]", SelectorType.ID);
        });
        Then("^a validation message should be displayed$", () -> {
            assertTrue(isElementPresent("validationSummary", SelectorType.ID));
        });
        When("^I click the no radio button for the \"([^\"]*)\" question$", (String arg0) -> {
            click("//*[@id=\"responsibilities\"]//input[@value='N']", SelectorType.XPATH);

        });
        Then("^the guidance text should be displayed$", () -> {
            String notDirectorText = findElement("//*[@id='responsibilities']/fieldset[2]/div[2]", SelectorType.XPATH, 10).getText();
            assertTrue(isTextPresent(notDirectorText,30));
        });
        Then("^the optional wording should not be displayed on the page$", (DataTable arg) -> {
            List<String> sections = arg.asList(String.class);
            for (String section : sections) {
                assertTrue(Browser.navigate().findElements(By.xpath(String.format("//*[@id=\"%s\"]/div/div", section))).stream().noneMatch(x -> x.getText().contains("(optional)")));
            }
        });
        And("^i navigate to the transport managers details page$", () -> {
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.selfServeNavigation.navigateToPage("application", "Transport Managers");
            click("//*[@name='table[action]']", SelectorType.XPATH);
            waitForTitleToBePresent("Add Transport Manager");
            selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, 1);
            click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
        });
        And("^the section buttons should not be displayed$", (DataTable table) -> {
            List<String> sections = table.asList(String.class);
            for (String button : sections) {
                assertTrue(Browser.navigate().findElements(By.xpath("//button")).stream().noneMatch(x -> x.getText().contains(button)));
            }
        });
        Then("^the section buttons should be displayed$", (DataTable table) -> {
            List<String> sections = table.asList(String.class);
            for (String button : sections) {
                assertTrue(Browser.navigate().findElements(By.xpath("//button")).stream().anyMatch(x -> x.getText().contains(button)));
            }
        });
    }
}