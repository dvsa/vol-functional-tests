package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.Journeys.APIJourneySteps;
import org.dvsa.testing.framework.Utils.Generic.GenericUtils;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import java.sql.Driver;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TMDetails extends BasePage implements En {

    public TMDetails(World world) {
        world.APIJourneySteps = new APIJourneySteps(world);
        world.genericUtils = new GenericUtils(world);
        Given("^I have a new application$", () -> {
            world.APIJourneySteps.registerAndGetUserDetails();
            world.APIJourneySteps.createPartialApplication();
        });
        And("^the transport manager is the operator$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getApplicationNumber());
            world.UIJourneySteps.nominateOperatorUserAsTransportManager(1);
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
            isTextPresent(arg0, 10);
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
            assertTrue(isTextPresent(notDirectorText, 10));
        });
        Then("^the optional wording should not be displayed on the page$", (DataTable arg) -> {
            List<String> sections = arg.asList(String.class);
            for(String section : sections){
                assertTrue(Browser.navigate().findElements(By.xpath(String.format("//*[@id=\"%s\"]/div/div", section))).stream().noneMatch(x -> x.getText().contains("(optional)")));
            }
        });
        And("^i navigate to the transport managers details page$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getApplicationNumber());
            world.UIJourneySteps.navigateToTransportManagersPage();
            click("//*[@name='table[action]']", SelectorType.XPATH);
            waitForTextToBePresent("Add Transport Manager");
            selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, 1);
            click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
        });
        And("^the section buttons should not be displayed$", (DataTable table) -> {
            List<String> sections = table.asList(String.class);
            for(String button : sections){
                assertTrue(Browser.navigate().findElements(By.xpath("//button")).stream().noneMatch(x -> x.getText().contains(button)));
            }
        });
        Then("^the section buttons should be displayed$", (DataTable table) -> {
            List<String> sections = table.asList(String.class);
            for(String button : sections){
                assertTrue(Browser.navigate().findElements(By.xpath("//button")).stream().anyMatch(x -> x.getText().contains(button)));
            }
        });
        When("^a self-serve user adds a TM$", () -> {
            world.UIJourneySteps.navigateToExternalUserLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getLicenceNumber());
            clickByLinkText("Transport Managers");
            waitForTextToBePresent("Transport Managers");
            clickByLinkText("change your licence");
            waitForTextToBePresent("Applying to change a licence");
            click("//*[@id='form-actions[submit]']",SelectorType.XPATH);
            waitForTextToBePresent("Add Transport Manager");
            click("//*[@id='add']",SelectorType.XPATH);
            selectValueFromDropDownByIndex("data[registeredUser]", SelectorType.ID, 1);
            click("//*[@id='form-actions[continue]']", SelectorType.XPATH);
//            enterText("dob_day", String.valueOf(getPastDayOfMonth(5)), SelectorType.ID);
//            enterText("dob_month", String.valueOf(getCurrentMonth()), SelectorType.ID);
//            enterText("dob_year", String.valueOf(getPastYear(20)), SelectorType.ID);
//            enterText("birthPlace","Test",SelectorType.ID);
//            world.UIJourneySteps.addAddressDetails();
            world.UIJourneySteps.addTransportManagerDetails();
//            click("//*[@id='5ce5144507a59']",SelectorType.XPATH);
//            click("//*[@id='responsibilities[isOwner]']",SelectorType.XPATH);
//            Browser.getDriver().findElements(By.xpath("//*[contains(@id,'responsibilities[hoursOfWeek]')]")).forEach(x -> {
//                        try {
//                            enterText(x.getText(),"3",SelectorType.XPATH);
//                        } catch (IllegalBrowserException e) {
//                            e.printStackTrace();
//                        }
//                    }
//            );
//            click("//*[@id='5ce5144508028']", SelectorType.XPATH);
//            click("//*[@id='5ce514450ae7b']", SelectorType.XPATH);
//            click("//*[@id='5ce514450b12b']", SelectorType.XPATH);
//            click("//*[@id='5ce514450b303']", SelectorType.XPATH);
//            click("//*[@id='form-actions[submit]']", SelectorType.XPATH);
//            click("form-actions[send]", SelectorType.ID);
//            waitForTextToBePresent("Transport Managers");
        });
    }
}