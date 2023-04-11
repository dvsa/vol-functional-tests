package org.dvsa.testing.framework.stepdefs.vol;

import org.apache.hc.core5.http.HttpException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.system.Properties;
import io.cucumber.java.en.*;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.lib.url.utils.EnvironmentType;
import org.openqa.selenium.WebElement;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ManageVehicle extends BasePage {
    World world;
    private EnvironmentType env = EnvironmentType.getEnum(Properties.get("env", true));
    public ManageVehicle(World world) {
        this.world = world;
    }

    @When("I navigate to manage vehicle page on an application")
    public void iNavigateToManageVehiclePageOnAnApplication(){
        world.dvlaJourney.navigateToManageVehiclesPage("application");
    }

    @When("I navigate to manage vehicle page on a licence")
    public void iNavigateToManageVehiclePageOnALicence(){
        world.dvlaJourney.navigateToManageVehiclesPage("licence");
    }

    @When("I navigate to manage vehicle page on a variation")
    public void iNavigateToManageVehiclePageOnAVariation(){
        world.dvlaJourney.navigateToManageVehiclesPage("variation");
    }

    @Then("the add vehicle page should display licence number")
    public void theAddVehiclePageShouldDisplayLicenceNumber(){
        assertEquals(world.applicationDetails.getLicenceNumber(), getText("licence", SelectorType.ID));
    }

    @And("choose to add a {string} vehicle")
    public void chooseToAddAVehicle(String VRM){
        world.UIJourney.addAVehicle(VRM);
        waitAndClick("confirm", SelectorType.ID);
        if(isTextPresent(String.format("%s is specified on another licence.", VRM))){
            findSelectAllRadioButtonsByValue("yes");
            clickById("next");
        }
    }

    @And("{string} heading")
    public void heading(String heading){
        assertEquals(heading, getText("h1", SelectorType.CSS));
    }

    @And("I search without entering a registration number")
    public void iSearchWithoutEnteringARegistrationNumber() {
        world.UIJourney.addAVehicle("");
    }

    @Then("An error message should be displayed")
    public void anErrorMessageShouldBeDisplayed() {
        isElementPresent("//div[@class=\"govuk-error-summary\"]", SelectorType.XPATH);
        isTextPresent("Enter a Vehicle Registration Mark");
    }

    @When("I search for a valid {string} registration")
    public void iSearchForAValidRegistration(String vrm) {
        world.dvlaJourney.VRM = vrm;
        waitAndClick("//*[contains(text(),'Add a vehicle')]", SelectorType.XPATH);
        waitAndClick("next", SelectorType.ID);
        enterText("vehicle-search[search-value]", SelectorType.NAME, world.dvlaJourney.VRM);
        waitAndClick("vehicle-search[submit]", SelectorType.NAME);
    }

    @Then("the vehicle summary should be displayed on the page:")
    public void theVehicleSummaryShouldBeDisplayedOnThePage(List<String> table) {
        isTextPresent(String.format("A vehicle has been found with registration %s", world.dvlaJourney.VRM));
        for (String columns : table) {
            isTextPresent(columns);
        }
    }

    @And("the vehicle details should not be empty")
    public void theVehicleDetailsShouldNotBeEmpty() {
        List<WebElement> vehicleDetails =
                findElements("//*[@class='govuk-table']//tbody[@class='govuk-table__body']//ancestor::tr[@class='govuk-table__row']//following-sibling::td", SelectorType.XPATH);
        for (WebElement element : vehicleDetails)
            assertNotNull(element.getText());
    }

    @Then("the following should be displayed:")
    public void theFollowingShouldBeDisplayed(List<String> headers) {
        for (String header : headers) {
            isTextPresent(header);
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
    public void iChooseToRemoveAVehicle() {
        world.UIJourney.removeVehicle();
    }

    @And("I choose to reprint a vehicle disc")
    public void iChooseToReprintAVehicleDisc() {
        world.dvlaJourney.navigateToReprintVehicleDiscPage();
    }

    @And("I want to confirm a vehicle removal")
    public void iWantToConfirmAVehicleRemoval() {
        world.UIJourney.vehicleRemovalConfirmationPage();
    }

    @And("I choose to transfer a vehicle")
    public void iChooseToTransferAVehicle() {
        world.dvlaJourney.navigateToTransferVehiclePage();
    }

    @When("i transfer a vehicle to an assumed licence")
    public void iTransferAVehiclesToAnAssumedLicence() {
        world.dvlaJourney.navigateToTransferVehiclePage();
        assertTrue(isTextPresent("All selected vehicles will be transferred to the licence:"));
        world.dvlaJourney.completeDVLAPageAndStoreValue("Y", "N", "N");
        world.dvlaJourney.completeDVLAConfirmationPageAndCheckVRM("Are you sure you want to transfer this vehicle to licence");
    }

    @When("i transfer a vehicle to a specified licence")
    public void iTransferAVehicleAToASpecifiedLicence() {
        world.dvlaJourney.navigateToTransferVehiclePage();
        assertTrue(isTextPresent("Select the licence that you want to transfer your vehicles to"));
        assertEquals("Select a licence", getSelectedTextFromDropDown("//select[@id='select-a-licence']", SelectorType.XPATH));
        selectValueFromDropDownByIndex("select-a-licence", SelectorType.ID, 1);
        world.dvlaJourney.completeDVLAPageAndStoreValue("Y", "N", "N");
        world.dvlaJourney.completeDVLAConfirmationPageAndCheckVRM("Are you sure you want to transfer this vehicle to licence");
    }

    @When("i transfer all the vehicles from my licence")
    public void iTransferAllTheVehiclesFromMyLicence() {
        world.dvlaJourney.navigateToTransferVehiclePage();
        assertTrue(isTextPresent("All selected vehicles will be transferred to the licence:"));
        world.dvlaJourney.completeDVLAPageAndStoreAllValues("Y", "N");
        world.dvlaJourney.completeDVLAConfirmationPageAndCheckAllVRMs("Are you sure you want to transfer these vehicles to licence");
    }

    @When("I reprint a vehicle disc")
    public void iReprintAVehicleDisc() throws HttpException {
        world.dvlaJourney.navigateToReprintVehicleDiscPage();
        world.dvlaJourney.completeDVLAPageAndStoreValue("Y", "Y", "N");
        world.dvlaJourney.completeDVLAConfirmationPageAndCheckVRM("Are you sure you want to reprint the disc for this vehicle");
        world.updateLicence.printLicenceDiscs();
    }

    @When("i search for and reprint a vehicle disc")
    public void iSearchForAndReprintAVehicleDisc() throws HttpException {
        world.dvlaJourney.navigateToReprintVehicleDiscPage();
        world.dvlaJourney.completeDVLAPageAndStoreValue("Y", "Y", "Y");
        world.dvlaJourney.completeDVLAConfirmationPageAndCheckVRM("Are you sure you want to reprint the disc for this vehicle");
        world.updateLicence.printLicenceDiscs();
    }

    @When("I reprint all my discs")
    public void iReprintAllMyDiscs() throws HttpException {
        world.dvlaJourney.navigateToReprintVehicleDiscPage();
        world.dvlaJourney.completeDVLAPageAndStoreAllValues("Y", "Y");
        world.dvlaJourney.completeDVLAConfirmationPageAndCheckAllVRMs("Are you sure you want to reprint discs for these vehicles");
        world.updateLicence.printLicenceDiscs();
    }

    @Then("the {string} confirmation banner should appear")
    public void theConfirmationBannerShouldAppear(String bannerValue) {
        String banner = getText("//h1[@class='govuk-panel__title']", SelectorType.XPATH);
        assertTrue(banner.contains(bannerValue));
    }

    @Then("the {string} confirmation body should appear")
    public void theConfirmationBodyShouldAppear(String bannerValue) {
        String banner = getText("//div[@class='govuk-panel__body']", SelectorType.XPATH);
        assertTrue(banner.contains(bannerValue));
    }

    @Then("a {string} error banner should appear")
    public void aErrorBannerShouldAppear(String bannerValue) {
        String banner = getText("//p[@role='alert']", SelectorType.XPATH);
        assertTrue(banner.contains(bannerValue));
    }

    @And("I want to confirm a vehicle disc reprint")
    public void iWantToConfirmAVehicleDiscReprint() {
        world.dvlaJourney.navigateToReprintVehicleDiscPage();
        click("//input[@type='checkbox']", SelectorType.XPATH);
        click("//*[@name='formActions[action]']", SelectorType.XPATH);
    }

    @And("I want to confirm a vehicle transfer")
    public void iWantToConfirmAVehicleTransfer() {
        world.dvlaJourney.navigateToTransferVehiclePage();
        click("//input[@type='checkbox']", SelectorType.XPATH);
        click("//*[@name='formActions[action]']", SelectorType.XPATH);
    }

    @And("the licence discs number should be updated")
    public void theLicenceDiscsNumberShouldBeUpdated() {
        world.dvlaJourney.navigateToReprintVehicleDiscPage();
        world.dvlaJourney.newDiscNumber = getText(
                String.format("//tr[*//a[contains(text(),'%s')]]//td[4]", world.dvlaJourney.VRM), SelectorType.XPATH);
        assertNotEquals(world.dvlaJourney.newDiscNumber, world.dvlaJourney.previousDiscNumber);
        assertFalse(isTextPresent(world.dvlaJourney.previousDiscNumber));
    }

    @And("i search and the licence discs number should be updated")
    public void iSearchAndTheLicenceDiscsNumberShouldBeUpdated() {
        world.dvlaJourney.navigateToReprintVehicleDiscPage();
        world.dvlaJourney.searchForExactVRM(world.dvlaJourney.VRM);
        world.dvlaJourney.newDiscNumber = getText(
                String.format("//tr[*//a[contains(text(),'%s')]]//td[4]", world.dvlaJourney.VRM), SelectorType.XPATH);
        assertNotEquals(world.dvlaJourney.newDiscNumber, world.dvlaJourney.previousDiscNumber);
        assertFalse(isTextPresent(world.dvlaJourney.previousDiscNumber));
    }

    @And("all the licence discs number should be updated")
    public void theAllTheLicenceDiscsNumberShouldBeUpdated() {
        world.dvlaJourney.navigateToReprintVehicleDiscPage();
        for (int i = 0; i < world.createApplication.getHgvVRMs().length; i++) {
            world.dvlaJourney.newDiscNumber = getText(
                    String.format("//tr[*//a[contains(text(),'%s')]]//td[4]", world.dvlaJourney.allVRMs.get(i)), SelectorType.XPATH);
            assertNotEquals(world.dvlaJourney.newDiscNumber, world.dvlaJourney.previousDiscNumber);
            assertFalse(isTextPresent(world.dvlaJourney.previousDiscNumber));
        }
    }

    @When("I clicks submit on {string} without checking a checkbox")
    public void iClicksSubmitWithoutCheckingACheckbox(String page) {
        switch (page) {
            case "remove":
                world.dvlaJourney.navigateToRemoveVehiclePage();
                break;
            case "reprint":
                world.dvlaJourney.navigateToReprintVehicleDiscPage();
                break;
            case "transfer":
                world.dvlaJourney.navigateToTransferVehiclePage();
                break;
        }
        click("//*[@name='formActions[action]']", SelectorType.XPATH);
    }

    @Then("the standard {string} errors appear")
    public void theVehicleDiscErrorsAppear(String error) {
        String errorTitle = "There is a problem";
        assertTrue(isElementPresent(String.format("//div[@class='validation-summary']/h2[contains(text(),'%s')]", errorTitle), SelectorType.XPATH));
        assertTrue(isElementPresent(String.format("//div[@class='validation-summary']//a[contains(text(),'%s')]", error), SelectorType.XPATH));
        assertTrue(isElementPresent(String.format("//div[@class='validation-wrapper']//p[contains(text(),'%s')]", error), SelectorType.XPATH));
    }

    @And("i remove a vehicle")
    public void iRemoveAVehicle() {
        world.dvlaJourney.navigateToRemoveVehiclePage();
        world.dvlaJourney.completeDVLAPageAndStoreValue("Y", "N", "N");
        world.dvlaJourney.completeDVLAConfirmationPageAndCheckVRM("Are you sure you want to remove the vehicle from your licence?");
    }

    @And("the vehicle should no longer be present")
    public void theVehicleShouldNoLongerBePresent() {
        world.dvlaJourney.navigateToRemoveVehiclePage();
        List<WebElement> remainingVRMs = findElements("//td//a", SelectorType.XPATH);
        for (WebElement VRM : remainingVRMs) {
            assertNotEquals(VRM.getText(), (world.dvlaJourney.VRM));
        }
    }

    @And("i search and remove a vehicle")
    public void iSearchAndRemoveAVehicle() {
        world.dvlaJourney.navigateToRemoveVehiclePage();
        world.dvlaJourney.completeDVLAPageAndStoreValue("Y", "N", "Y");
        world.dvlaJourney.completeDVLAConfirmationPageAndCheckVRM("Are you sure you want to remove the vehicle from your licence?");
    }

    @And("i search and the vehicle should no longer be present")
    public void iSearchAndTheVehicleShouldNoLongerBePresent() {
        world.dvlaJourney.navigateToRemoveVehiclePage();
        searchForVRMToRemove(null);
        assertFalse(isTextPresent(world.dvlaJourney.VRM));
        assertTrue(isTextPresent("No vehicle can be found with that Vehicle Registration Mark"));
    }

    @And("i remove all my vehicles")
    public void iRemoveAllMyVehicles() {
        world.dvlaJourney.navigateToRemoveVehiclePage();
        world.dvlaJourney.completeDVLAPageAndStoreAllValues("Y", "Y");
        world.dvlaJourney.completeDVLAConfirmationPageAndCheckAllVRMs("Are you sure you want to remove the vehicles from your licence?");
    }

    @Then("the switchboard only views add vehicle and view vehicle radio buttons")
    public void theSwitchboardOnlyViewsAddVehicleAndViewVehicleRadioButtons() {
        List<WebElement> switchboardElements = findElements("//label[@class='govuk-label govuk-radios__label']", SelectorType.XPATH);
        for (WebElement radioButton : switchboardElements) {
            assertNotEquals(radioButton.getText(), "Remove a vehicle");
            assertNotEquals(radioButton.getText(), "Reprint vehicle disc");
        }
        assertEquals(switchboardElements.get(0).getText(), "Add a vehicle");
        assertEquals(switchboardElements.get(1).getText(), "View the vehicles you have removed from your licence");
    }

    @Then("the {string} should be displayed on the page")
    public void theShouldBeDisplayedOnThePage(String vrm) {
        assertTrue(isTextPresent(String.format("Vehicle %s has been added", vrm)));
    }

    @Then("i remove the {int} extra vehicles")
    public void iRemoveTheExtraVehicles(int numberOfVehicles) throws InterruptedException {
        isTextPresent("");
        world.selfServeNavigation.navigateToPage("variation", SelfServeSection.VEHICLES);
        for (int i = 0; i < numberOfVehicles; i++) {
            waitAndClick("//button[contains(@name, 'table[action][delete]')]", SelectorType.XPATH);
            waitForTextToBePresent("Are you sure you want to remove these vehicle(s)?");
            world.UIJourney.clickSubmit();
            waitForElementToBeClickable("//input[@value='Remove']", SelectorType.XPATH);
            Thread.sleep(2000);
        }
    }
    @And("the vehicle {string} does not exist on the licence")
    public void theVehicleDoesNotExistOnTheLicence(String vrm) {
        world.dvlaJourney.navigateToRemoveVehiclePage();
        searchForVRMToRemove(vrm);
        if(isTextPresent(vrm)){
            waitAndClick("//*[@type='checkbox']",SelectorType.XPATH);
            waitAndClick("formActions[action]",SelectorType.NAME);
            waitAndClick("//*[contains(text(), 'Yes')]", SelectorType.XPATH);
            waitAndClick("next",SelectorType.ID);
        }else {
            clickByLinkText("manage your vehicles");
        }
    }

    private void searchForVRMToRemove(String vrm) {
        String vehicleRegistrationMark;
        if(vrm == null) {
            vehicleRegistrationMark = world.dvlaJourney.VRM;
        } else{
            vehicleRegistrationMark = vrm;
        }
        enterText("vehicleSearch[search-value]", SelectorType.NAME, vehicleRegistrationMark);
        click("vehicleSearch[submit]", SelectorType.NAME);
    }
}