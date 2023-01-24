package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import activesupport.driver.Browser;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import io.cucumber.datatable.DataTable;
import org.dvsa.testing.framework.enums.SelfServeNavBar;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TMDetails extends BasePage implements En {
    private final World world;

    public TMDetails (World world) {this.world = world;}

    @And("i navigate to the admin transport managers details page")
    public void iNavigateToTheAdminTransportManagersDetailsPage() {
        world.selfServeNavigation.navigateToNavBarPage(SelfServeNavBar.MANAGE_USERS);
       String admin = getAttribute("*//td[contains(text(),'Administrator')]", SelectorType.XPATH, "data-heading");
        world.selfServeNavigation.navigateToPage("application", SelfServeSection.TRANSPORT_MANAGERS);
        click("//*[@name='table[action]']", SelectorType.XPATH);
        waitForTitleToBePresent("Add Transport Manager");
        selectValueFromDropDown("data[registeredUser]", SelectorType.ID, admin);
        world.UIJourney.clickContinue();
    }

    @Then("the optional wording should not be displayed on the page")
    public void theOptionalWordingShouldNotBeDisplayedOnThePage(DataTable arg) {
        List<String> sections = arg.asList(String.class);
        for (String section : sections) {
            assertTrue(Browser.navigate().findElements(By.xpath(String.format("//*[@id=\"%s\"]/div/div", section))).stream().noneMatch(x -> x.getText().contains("(optional)")));
        }
    }

    @And("the section buttons should not be displayed")
    public void theSectionButtonsShouldNotBeDisplayed(DataTable dataTable) {
        List<String> buttonsOnPage = dataTable.asList(String.class);
        for (String button : buttonsOnPage) {
            assertTrue(Browser.navigate().findElements(By.xpath("//button")).stream().noneMatch(x -> x.getText().contains(button)));
        }
    }

    @When("I select yes to all radio buttons")
    public void iSelectYesToAllRadioButtons() {
        findSelectAllRadioButtonsByValue("Y");
    }

    @Then("the section buttons should be displayed")
    public void theSectionButtonsShouldBeDisplayed(DataTable table) {
        List<String> sections = table.asList(String.class);
        for (String button : sections) {
            assertTrue(Browser.navigate().findElements(By.xpath("//button")).stream().anyMatch(x -> x.getText().contains(button)));
        }
    }

    @When("I click on the {string} button")
    public void iClickOnTheButton(String button) {
        findSelectAllRadioButtonsByValue("Y");
        click(String.format("//*[@data-label=\"%s\"]", button), SelectorType.XPATH);
    }

    @Then("I should see the {string} page")
    public void iShouldSeeThePage(String page) {
        assertTrue(Browser.navigate().getCurrentUrl().contains(page));
    }

    @And("page title {string} should be displayed on page")
    public void pageTitleShouldBeDisplayedOnPage(String arg0) {
        assertTrue(isTextPresent(arg0));
        clickByLinkText("Back");
    }

    @When("the users attempts to save without entering any data")
    public void theUsersAttemptsToSaveWithoutEnteringAnyData() {
        world.UIJourney.clickSubmit();
    }

    @Then("a validation message should be displayed")
    public void aValidationMessageShouldBeDisplayed() {
        assertTrue(isElementPresent("validationSummary", SelectorType.ID));
    }

    @When("I click the no radio button for the {string} question")
    public void iClickTheNoRadioButtonForTheQuestion(String arg0) {
        click("//*[@id=\"responsibilities\"]//input[@value='N']", SelectorType.XPATH);
    }

    @Then("the guidance text should be displayed")
    public void theGuidanceTextShouldBeDisplayed() {
        String notDirectorText = findElement("//*[@id='responsibilities']/fieldset[2]/div[2]", SelectorType.XPATH, 10).getText();
        assertTrue(isTextPresent(notDirectorText));
    }
}
