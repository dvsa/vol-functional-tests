package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import activesupport.driver.Browser;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import enums.OperatorType;
import enums.UserRoles;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.LoginPage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.util.List;

public class ManageVehicle extends BasePage {
    World world;
    String vrm;

    public ManageVehicle(World world){
         this.world = world;
    }

    @Given("I have a new {string} application")
    public void iHaveANewApplication(String operatorType) {
        world.createLicence.setOperatorType(OperatorType.getEnum(operatorType).getName());
        world.APIJourneySteps.registerAndGetUserDetails(UserRoles.EXTERNAL.getUserRoles());
        world.APIJourneySteps.createPartialApplication();
    }

    @When("I navigate to manage vehicle page")
    public void iNavigateToManageVehiclePage() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToLogin( world.registerUser.getLoginId(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Vehicles");
        String URL = Browser.navigate().getCurrentUrl();
        String newURL = URL.substring(0, URL.length()-2);
        Browser.navigate().get(newURL);
    }

    @Then("the add vehicle page should display licence number")
    public void theAddVehiclePageShouldDisplayLicenceNumber() throws MalformedURLException, IllegalBrowserException {
        Assert.assertEquals(world.createLicence.getLicenceNumber(),getText("licence",SelectorType.ID));
    }

    @And("choose to add a {string} vehicle")
    public void chooseToAddAVehicle(String VRM) throws MalformedURLException, IllegalBrowserException {
        world.UIJourneySteps.addAVehicle(VRM);
    }

    @And("{string} heading")
    public void heading(String heading) throws MalformedURLException, IllegalBrowserException {
        Assert.assertEquals(heading, getText("h1", SelectorType.CSS));
    }

    @And("I search without entering a registration number")
    public void iSearchWithoutEnteringARegistrationNumber() throws MalformedURLException, IllegalBrowserException {
        click("//*[contains(text(),'Find vehicle')]",SelectorType.XPATH);
    }

    @Then("An error message should be displayed")
    public void anErrorMessageShouldBeDisplayed() throws MalformedURLException, IllegalBrowserException {
        isElementPresent("//div[@class=\"govuk-error-summary\"]",SelectorType.XPATH);
        isTextPresent("Enter a Vehicle Registration Mark",60);
    }

    @When("I search for a valid {string} registration")
    public void iSearchForAValidRegistration(String vrm) throws MalformedURLException, IllegalBrowserException {
        this.vrm = vrm;
        enterText("registration-mark",vrm,SelectorType.ID);
    }

    @Then("the vehicle summary should be displayed on the page:")
    public void theVehicleSummaryShouldBeDisplayedOnThePage(List<String> table) {
        isTextPresent(String.format("A vehicle has been found with registration %s",this.vrm), 60);
        for(String columns : table) {
            isTextPresent(columns, 60);
        }
    }

    @And("the vehicle details should not be empty")
    public void theVehicleDetailsShouldNotBeEmpty() throws MalformedURLException, IllegalBrowserException {
        List<WebElement> vehicleDetails =
                findElements("//*[@class='govuk-table']//tbody[@class='govuk-table__body']//ancestor::tr[@class='govuk-table__row']//following-sibling::td",SelectorType.XPATH);
        for(WebElement element : vehicleDetails)
            Assert.assertNotNull(element.getText());
    }

    @Then("the following should be displayed:")
    public void theFollowingShouldBeDisplayed(List<String> headers) {
        for(String header : headers){
            isTextPresent(header,60);
        }
    }

    @And("I add a vehicle belonging to another licence")
    public void iAddAVehicleBelongingToAnotherLicence() {
        //Todo
    }

    @Then("I should be prompted that vehicle belongs to another licence")
    public void iShouldBePromptedThatVehicleBelongsToAnotherLicence() {
        //Todo
    }

    @When("I navigate to add existing vehicle page")
    public void iNavigateToAddExistingVehiclePage() throws MalformedURLException, IllegalBrowserException {
        Browser.navigate().get("https://ssap1.da.olcs.dev-dvsacloud.uk/licence/545765/vehicle/add/");
        LoginPage.signIn("James289.Witherspoon6212144", "BunDog=336MixZoo");
        waitAndEnterText("vehicle-search[search-value]",SelectorType.ID,"F95 JGE");
        waitAndClick("vehicle-search[submit]",SelectorType.ID);
        waitAndClick("confirm",SelectorType.ID);
    }

    @And("I choose to remove a vehicle")
    public void iChooseToRemoveAVehicle() throws MalformedURLException, IllegalBrowserException {
        world.UIJourneySteps.removeVehicle();
    }

    @And("I want to confirm a vehicle removal")
    public void iWantToConfirmAVehicleRemoval() throws MalformedURLException, IllegalBrowserException {
        world.UIJourneySteps.vehicleRemovalConfirmationPage();
    }

    @And("choose to transfer a vehicle")
    public void chooseToTransferAVehicle() throws InterruptedException {
        wait();
    }
}