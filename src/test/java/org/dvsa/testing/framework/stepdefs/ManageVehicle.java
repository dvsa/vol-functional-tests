package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import activesupport.IllegalBrowserException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import enums.OperatorType;
import enums.UserRoles;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;
import org.junit.Assert;

import java.net.MalformedURLException;
import java.nio.channels.Selector;
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
        world.selfServeNavigation.navigateToLogin( world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
        world.selfServeNavigation.navigateToPage("application", "vehicles");
    }

    @Then("the add vehicle page should display licence number")
    public void theAddVehiclePageShouldDisplayLicenceNumber() throws MalformedURLException, IllegalBrowserException {
        Assert.assertEquals(world.createLicence.getLicenceNumber(),getText("licence",SelectorType.ID));
    }

    @And("choose to add a vehicle")
    public void chooseToAddAVehicle() throws MalformedURLException, IllegalBrowserException {
        world.UIJourneySteps.addAVehicle();
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

    @Then("the vehicle details should be displayed on the page:")
    public void theVehicleDetailsShouldBeDisplayedOnThePage(List<String> table) {
        isTextPresent(String.format("A vehicle has been found with registration %s",this.vrm), 60);
        for(String columns : table){
            isTextPresent(columns,60);
        }
    }
}