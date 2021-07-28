package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import io.cucumber.datatable.DataTable;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;
import org.openqa.selenium.By;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TMDetails extends BasePage implements En {

    String fileName = "src/test/resources/";

    public TMDetails(World world) {
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
        Then("^I should see the \"([^\"]*)\" page$", (String page) -> {
            assertTrue(Browser.navigate().getCurrentUrl().contains(page));
        });
        And("^page title \"([^\"]*)\" should be displayed on page$", (String arg0) -> {
            assertTrue(isTextPresent(arg0));
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
            assertTrue(isTextPresent(notDirectorText));
        });
        Then("^the optional wording should not be displayed on the page$", (DataTable arg) -> {
            List<String> sections = arg.asList(String.class);
            for (String section : sections) {
                assertTrue(Browser.navigate().findElements(By.xpath(String.format("//*[@id=\"%s\"]/div/div", section))).stream().noneMatch(x -> x.getText().contains("(optional)")));
            }
        });
        And("^i navigate to the admin transport managers details page$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToNavBarPage("manage users");
            String admin = getAttribute("*//td[contains(text(),'Administrator')]/../td[1]/input", SelectorType.XPATH, "value");
            world.selfServeNavigation.navigateToPage("application", "Transport Managers");
            click("//*[@name='table[action]']", SelectorType.XPATH);
            waitForTitleToBePresent("Add Transport Manager");
            selectValueFromDropDown("data[registeredUser]", SelectorType.ID, admin);
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
        Then("^accounts should be created$", () -> {
            world.genericUtils.writeToFile(world.createApplication.getTransportManagerApplicationId(), world.globalMethods.getLoginPassword(), fileName.concat("TM.csv"));
        });
    }
}