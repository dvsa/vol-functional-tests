package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.Assert.assertEquals;

public class GoodVarIncreaseVehicle extends BasePage implements En {
    private final World world;

    public GoodVarIncreaseVehicle (World world) {this.world = world;}

    @Then("An error message should appear")
    public void anErrorMessageShouldAppear() {
        isTextPresent("//*[@id=\"OperatingCentres\"]/fieldset[3]/div[1]/div/p");
    }

    @When("i increase my vehicle authority count")
    public void iIncreaseMyVehicleAuthorityCount() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        world.UIJourney.changeVehicleReq(String.valueOf(world.createApplication.getNoOfVehiclesRequested() + 1 ));
        world.UIJourney.changeVehicleAuth(String.valueOf(world.createApplication.getNoOfVehiclesRequested() + 1 ));
    }

    @Then("a status of update required should be shown next to financial evidence")
    public void aStatusOfUpdateRequiredShouldBeShownNextToFinancialEvidence() {
        waitForElementToBePresent("//*[@id='overview-item__financial_evidence']");
    }

    @When("A selfserve user increases the vehicle required count by invalid characters")
    public void aSelfserveUserIncreasesTheVehicleRequiredCountByInvalidCharacters() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        world.UIJourney.changeVehicleReq("+6");
    }

    @When("A selfserve user increases the vehicle authority by invalid charecters")
    public void aSelfserveUserIncreasesTheVehicleAuthorityByInvalidCharecters() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
        world.UIJourney.changeLicenceForVariation();
        world.UIJourney.changeVehicleAuth("+6");
    }

    @Then("An error should appear")
    public void anErrorShouldAppear() {
        isTextPresent("//*[@id=\"OperatingCentre\"]/fieldset[2]/div[1]/div/p");
    }

    @And("a selfserve user creates a variation and increases the vehicle authority count")
    public void aSelfserveUserCreatesAVariationAndIncreasesTheVehicleAuthorityCount() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
        clickByLinkText(world.applicationDetails.getLicenceNumber());
        world.UIJourney.changeVehicleReq(String.valueOf(world.createApplication.getNoOfVehiclesRequested() +2));
        world.UIJourney.changeVehicleAuth(String.valueOf(world.createApplication.getNoOfVehiclesRequested() + 2));
        world.UIJourney.updateFinancialInformation();
        world.UIJourney.signDeclarationForVariation();
    }

    @Then("the {string} fee should be paid")
    public void theFeeShouldBePaid(String arg0) {
        clickByLinkText("Fees");
        selectValueFromDropDown("//*[@id='status']", SelectorType.XPATH,"All");
        waitForTextToBePresent("Grant Fee for application");
        assertEquals(getText("//table//tr[td//text()[contains(., 'Variation Fee for application')]]//*[contains(@class,'status')]",SelectorType.XPATH),"PAID");
    }

    @And("a selfserve user creates a variation and adds an operating centre")
    public void aSelfserveUserCreatesAVariationAndAddsAnOperatingCentre() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
        world.UIJourney.changeLicenceForVariation();
        world.UIJourney.addNewOperatingCentreSelfServe(7,7);
        world.UIJourney.updateFinancialInformation();
        world.UIJourney.signDeclarationForVariation();
    }
}
