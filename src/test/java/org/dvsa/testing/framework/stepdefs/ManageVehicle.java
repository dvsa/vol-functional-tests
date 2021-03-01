package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.net.MalformedURLException;
import java.util.List;

import static org.junit.Assert.*;

public class ManageVehicle extends BasePage {
    World world;

    public ManageVehicle(World world){
         this.world = world;
    }

    @When("I navigate to manage vehicle page on an application")
    public void iNavigateToManageVehiclePageOnAnApplication() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToLogin( world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.dvlaJourneySteps.navigateToManageVehiclesPage("application");
    }

    @When("I navigate to manage vehicle page on a licence")
    public void iNavigateToManageVehiclePageOnALicence() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToLogin( world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.dvlaJourneySteps.navigateToManageVehiclesPage("licence");
    }

    @When("I navigate to manage vehicle page on a variation")
    public void iNavigateToManageVehiclePageOnAVariation() throws MalformedURLException, IllegalBrowserException {
        world.selfServeNavigation.navigateToLogin( world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.dvlaJourneySteps.navigateToManageVehiclesPage("variation");
    }

    @Then("the add vehicle page should display licence number")
    public void theAddVehiclePageShouldDisplayLicenceNumber() throws MalformedURLException, IllegalBrowserException {
        Assert.assertEquals(world.applicationDetails.getLicenceNumber(),getText("licence",SelectorType.ID));
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
        world.dvlaJourneySteps.VRM = vrm;
        enterText("registration-mark", world.dvlaJourneySteps.VRM, SelectorType.ID);
    }

    @Then("the vehicle summary should be displayed on the page:")
    public void theVehicleSummaryShouldBeDisplayedOnThePage(List<String> table) {
        isTextPresent(String.format("A vehicle has been found with registration %s", world.dvlaJourneySteps.VRM), 60);
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

    @And("I choose to remove a vehicle")
    public void iChooseToRemoveAVehicle() throws MalformedURLException, IllegalBrowserException {
        world.UIJourneySteps.removeVehicle();
    }

    @And("I choose to reprint a vehicle disc")
    public void iChooseToReprintAVehicleDisc() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToReprintVehicleDiscPage();
    }

    @And("I want to confirm a vehicle removal")
    public void iWantToConfirmAVehicleRemoval() throws MalformedURLException, IllegalBrowserException {
        world.UIJourneySteps.vehicleRemovalConfirmationPage();
    }

    @And("I choose to transfer a vehicle")
    public void iChooseToTransferAVehicle() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToTransferVehiclePage();
    }

    @When("i transfer a vehicle to an assumed licence")
    public void iTransferAVehiclesToAnAssumedLicence() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToTransferVehiclePage();
        assertTrue(isTextPresent("All selected vehicles will be transferred to the licence:", 10));
        world.dvlaJourneySteps.completeDVLAPageAndStoreValue("Y", "N", "N");
        world.dvlaJourneySteps.completeDVLAConfirmationPageAndCheckVRM("Are you sure you want to transfer this vehicle to licence");
    }

    @When("i transfer a vehicle to a specified licence")
    public void iTransferAVehicleAToASpecifiedLicence() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToTransferVehiclePage();
        assertTrue(isTextPresent("Select the licence that you want to transfer your vehicles to", 10));
        Select option = new Select(findElement("//select[@id='select-a-licence']", SelectorType.XPATH));
        assertEquals("Select a licence", option.getFirstSelectedOption().getText());
        selectValueFromDropDownByIndex("select-a-licence", SelectorType.ID, 1);
        world.dvlaJourneySteps.completeDVLAPageAndStoreValue("Y", "N", "N");
        world.dvlaJourneySteps.completeDVLAConfirmationPageAndCheckVRM("Are you sure you want to transfer this vehicle to licence");
    }

    @When("i transfer all the vehicles from my licence")
    public void iTransferAllTheVehiclesFromMyLicence() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToTransferVehiclePage();
        assertTrue(isTextPresent("All selected vehicles will be transferred to the licence:", 10));
        world.dvlaJourneySteps.completeDVLAPageAndStoreAllValues("Y", "N");
        world.dvlaJourneySteps.completeDVLAConfirmationPageAndCheckAllVRMs("Are you sure you want to transfer these vehicles to licence");
    }

    @When("I reprint a vehicle disc")
    public void iReprintAVehicleDisc() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToReprintVehicleDiscPage();
        world.dvlaJourneySteps.completeDVLAPageAndStoreValue("Y", "Y", "N");
        world.dvlaJourneySteps.completeDVLAConfirmationPageAndCheckVRM("Are you sure you want to reprint the disc for this vehicle");
        world.updateLicence.printLicenceDiscs();
    }

    @When("i search for and reprint a vehicle disc")
    public void iSearchForAndReprintAVehicleDisc() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToReprintVehicleDiscPage();
        world.dvlaJourneySteps.completeDVLAPageAndStoreValue("Y", "Y", "Y");
        world.dvlaJourneySteps.completeDVLAConfirmationPageAndCheckVRM("Are you sure you want to reprint the disc for this vehicle");
        world.updateLicence.printLicenceDiscs();
    }

    @When("I reprint all my discs")
    public void iReprintAllMyDiscs() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToReprintVehicleDiscPage();
        world.dvlaJourneySteps.completeDVLAPageAndStoreAllValues("Y", "Y");
        world.dvlaJourneySteps.completeDVLAConfirmationPageAndCheckAllVRMs("Are you sure you want to reprint discs for these vehicles");
        world.updateLicence.printLicenceDiscs();
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

    @And("I want to confirm a vehicle disc reprint")
    public void iWantToConfirmAVehicleDiscReprint() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToReprintVehicleDiscPage();
        click("//input[@type='checkbox']", SelectorType.XPATH);
        click("//*[@name='formActions[action]']", SelectorType.XPATH);
    }

    @And("I want to confirm a vehicle transfer")
    public void iWantToConfirmAVehicleTransfer() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToTransferVehiclePage();
        click("//input[@type='checkbox']", SelectorType.XPATH);
        click("//*[@name='formActions[action]']", SelectorType.XPATH);
    }

    @And("the licence discs number should be updated")
    public void theLicenceDiscsNumberShouldBeUpdated() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToReprintVehicleDiscPage();
        world.dvlaJourneySteps.newDiscNumber = getText(
                String.format("//tr[*//a[contains(text(),'%s')]]//td[4]", world.dvlaJourneySteps.VRM), SelectorType.XPATH);
        Assert.assertNotEquals(world.dvlaJourneySteps.newDiscNumber, world.dvlaJourneySteps.previousDiscNumber);
        Assert.assertFalse(isTextPresent(world.dvlaJourneySteps.previousDiscNumber, 10));
    }

    @And("i search and the licence discs number should be updated")
    public void iSearchAndTheLicenceDiscsNumberShouldBeUpdated() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToReprintVehicleDiscPage();
        world.dvlaJourneySteps.searchForExactVRM(world.dvlaJourneySteps.VRM);
        world.dvlaJourneySteps.newDiscNumber = getText(
                String.format("//tr[*//a[contains(text(),'%s')]]//td[4]", world.dvlaJourneySteps.VRM), SelectorType.XPATH);
        Assert.assertNotEquals(world.dvlaJourneySteps.newDiscNumber, world.dvlaJourneySteps.previousDiscNumber);
        Assert.assertFalse(isTextPresent(world.dvlaJourneySteps.previousDiscNumber, 10));
    }

    @And("all the licence discs number should be updated")
    public void theAllTheLicenceDiscsNumberShouldBeUpdated() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToReprintVehicleDiscPage();
        for (int i = 0; i < world.createApplication.getVehicleVRMs().length; i++) {
            world.dvlaJourneySteps.newDiscNumber = getText(
                    String.format("//tr[*//a[contains(text(),'%s')]]//td[4]", world.dvlaJourneySteps.allVRMs.get(i)), SelectorType.XPATH);
            Assert.assertNotEquals(world.dvlaJourneySteps.newDiscNumber, world.dvlaJourneySteps.previousDiscNumber);
            Assert.assertFalse(isTextPresent(world.dvlaJourneySteps.previousDiscNumber, 10));
        }
    }

    @When("I clicks submit on {string} without checking a checkbox")
    public void iClicksSubmitWithoutCheckingACheckbox(String page) throws MalformedURLException, IllegalBrowserException {
        switch (page) {
            case "remove":
                world.dvlaJourneySteps.navigateToRemoveVehiclePage();
                break;
            case "reprint":
                world.dvlaJourneySteps.navigateToReprintVehicleDiscPage();
                break;
            case "transfer":
                world.dvlaJourneySteps.navigateToTransferVehiclePage();
                break;
        }
        click("//*[@name='formActions[action]']", SelectorType.XPATH);
    }

    @Then("the standard {string} errors appear")
    public void theVehicleDiscErrorsAppear(String error) throws IllegalBrowserException {
        String errorTitle = "There is a problem";
        assertTrue(isElementPresent(String.format("//div[@class='validation-summary']/h2[contains(text(),'%s')]", errorTitle), SelectorType.XPATH));
        assertTrue(isElementPresent(String.format("//div[@class='validation-summary']//a[contains(text(),'%s')]", error), SelectorType.XPATH));
        assertTrue(isElementPresent(String.format("//div[@class='validation-wrapper']//p[contains(text(),'%s')]", error), SelectorType.XPATH));
    }

    @And("i remove a vehicle")
    public void iRemoveAVehicle() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToRemoveVehiclePage();
        world.dvlaJourneySteps.completeDVLAPageAndStoreValue("Y", "N", "N");
        world.dvlaJourneySteps.completeDVLAConfirmationPageAndCheckVRM("Are you sure you want to remove the vehicle from your licence?");
    }

    @And("the vehicle should no longer be present")
    public void theVehicleShouldNoLongerBePresent() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToRemoveVehiclePage();
        List<WebElement> remainingVRMs = findElements("//td//a", SelectorType.XPATH);
        for (WebElement VRM : remainingVRMs){
            assertNotEquals(VRM.getText(),(world.dvlaJourneySteps.VRM));
        }
    }

    @And("i search and remove a vehicle")
    public void iSearchAndRemoveAVehicle() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToRemoveVehiclePage();
        world.dvlaJourneySteps.completeDVLAPageAndStoreValue("Y", "N", "Y");
        world.dvlaJourneySteps.completeDVLAConfirmationPageAndCheckVRM("Are you sure you want to remove the vehicle from your licence?");
    }

    @And("i search and the vehicle should no longer be present")
    public void iSearchAndTheVehicleShouldNoLongerBePresent() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToRemoveVehiclePage();
        enterText("vehicleSearch[search-value]", world.dvlaJourneySteps.VRM, SelectorType.NAME);
        click("vehicleSearch[submit]", SelectorType.NAME);
        Assert.assertFalse(isTextPresent(world.dvlaJourneySteps.VRM, 10));
        Assert.assertTrue(isTextPresent("No vehicle can be found with that Vehicle Registration Mark", 10));
    }

    @And("i remove all my vehicles")
    public void iRemoveAllMyVehicles() throws MalformedURLException, IllegalBrowserException {
        world.dvlaJourneySteps.navigateToRemoveVehiclePage();
        world.dvlaJourneySteps.completeDVLAPageAndStoreAllValues("Y", "Y");
        world.dvlaJourneySteps.completeDVLAConfirmationPageAndCheckAllVRMs("Are you sure you want to remove the vehicles from your licence?");
    }

    @Then("the switchboard only views add vehicle and view vehicle radio buttons")
    public void theSwitchboardOnlyViewsAddVehicleAndViewVehicleRadioButtons() throws MalformedURLException, IllegalBrowserException {
        List<WebElement> switchboardElements = findElements("//label[@class='govuk-label govuk-radios__label']", SelectorType.XPATH);
        for (WebElement radioButton : switchboardElements) {
            assertNotEquals(radioButton.getText(),"Remove a vehicle");
            assertNotEquals(radioButton.getText(),"Reprint vehicle disc");
        }
        assertEquals(switchboardElements.get(0).getText(), "Add a vehicle");
        assertEquals(switchboardElements.get(1).getText(), "View the vehicles you have removed from your licence");
    }
}