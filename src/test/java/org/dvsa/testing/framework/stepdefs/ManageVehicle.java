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
import org.openqa.selenium.support.ui.Select;

import java.net.MalformedURLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @And("i choose to transfer a vehicle")
    public void iChooseToTransferAVehicle() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='transfer-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Transfer vehicles between your licences");
    }

    @When("i transfer a vehicle to an assumed licence")
    public void iTransferAVehiclesToAnAssumedLicence() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='transfer-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Transfer vehicles between your licences");
        assertTrue(isTextPresent("All selected vehicles will be transferred to the licence:", 10));
        String VRM = getText("//td//a", SelectorType.XPATH);
        click("//input[@type='checkbox']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Are you sure you want to transfer this vehicle to licence");
        assertTrue(isTextPresent(VRM, 10));
        click("//input[@id='option-yes']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Do you want to");
    }

    @When("i transfer a vehicle to a specified licence")
    public void iTransferAVehicleAToASpecifiedLicence() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='transfer-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Transfer vehicles between your licences");
        assertTrue(isTextPresent("Select the licence that you want to transfer your vehicles to", 10));
        String VRM = getText("//td//a", SelectorType.XPATH);
        click("//input[@type='checkbox']", SelectorType.XPATH);
        Select option = new Select(findElement("//select[@id='select-a-licence']", SelectorType.XPATH));
        assertEquals("Select a licence", option.getFirstSelectedOption().getText());
        selectValueFromDropDownByIndex("select-a-licence", SelectorType.ID, 1);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Are you sure you want to transfer this vehicle to licence");
        assertTrue(isTextPresent(VRM, 10));
        click("//input[@id='option-yes']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Do you want to");
    }

    @When("i transfer all the vehicles from my licence")
    public void iTransferAllTheVehiclesFromMyLicence() throws MalformedURLException, IllegalBrowserException {
        click("//input[@id='transfer-vehicle']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Transfer vehicles between your licences");
        assertTrue(isTextPresent("All selected vehicles will be transferred to the licence:", 10));
        List<WebElement> VRMElements = findElements("//td//a", SelectorType.XPATH);
        List<WebElement> checkboxElements = findElements("//input[@type='checkbox']", SelectorType.XPATH);
        String[] VRMs = new String[VRMElements.size()];
        for (int i = 0; i < VRMElements.size(); i++) {
            VRMs[i] = VRMElements.get(i).getText();
            checkboxElements.get(i).click();
        }
        click("//*[@type='submit']", SelectorType.XPATH);
        waitForTitleToBePresent("Are you sure you want to transfer these vehicles to licence");
        for (String VRM: VRMs) {
            assertTrue(isTextPresent(VRM, 10));
        }
        click("//input[@id='option-yes']", SelectorType.XPATH);
        click("//*[@type='submit']", SelectorType.XPATH);
    }

    @Then("the {string} confirmation banner should appear")
    public void theConfirmationBannerShouldAppear(String bannerValue) throws MalformedURLException, IllegalBrowserException {
        String banner = getText("//h1[@class='govuk-panel__title']", SelectorType.XPATH);
        assertTrue(banner.contains(bannerValue));
    }

    @Then("the {string} confirmation body should appear")
    public void theConfirmationBodyShouldAppear(String bannerValue) throws MalformedURLException, IllegalBrowserException {
        String banner = getText("//div[@class='govuk-panel__body']", SelectorType.XPATH);
        assertTrue(banner.contains(bannerValue));
    }

    @Then("a {string} error banner should appear")
    public void aErrorBannerShouldAppear(String bannerValue) throws MalformedURLException, IllegalBrowserException {
        String banner = getText("//p[@role='alert']", SelectorType.XPATH);
        assertTrue(banner.contains(bannerValue));
    }
}