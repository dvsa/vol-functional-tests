package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.newPages.BasePage;
import org.dvsa.testing.lib.newPages.enums.SelectorType;

import static org.junit.Assert.assertEquals;


public class GoodVarIncreaseVehicle extends BasePage implements En  {
    World world = new World();

    public GoodVarIncreaseVehicle(World world) {

        When("^i increase my vehicle authority count$", () -> {
         world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
         clickByLinkText(world.applicationDetails.getLicenceNumber());
         world.UIJourney.changeVehicleReq(String.valueOf(world.createApplication.getNoOfVehiclesRequested() + 1 ));
         world.UIJourney.changeVehicleAuth(String.valueOf(world.createApplication.getNoOfVehiclesRequested() + 1 ));
        });

        Then("^a status of update required should be shown next to financial evidence$", () -> {
            waitForElementToBePresent("//*[@id='overview-item__financial_evidence']");
        });

        When("^A selfserve user increases the vehicle required count by invalid characters$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            world.UIJourney.changeVehicleReq("+6");
        });
        Then("^An error message should appear$", () -> {
            isTextPresent("//*[@id=\"OperatingCentre\"]/fieldset[2]/div[1]/div/p");
        });

        When("^A selfserve user increases the vehicle authority by invalid charecters$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            world.UIJourney.changeVehicleReq(String.valueOf(world.createApplication.getNoOfVehiclesRequested()));
            world.UIJourney.changeVehicleAuth("+6");
        });
        Then("^An error should appear$", () -> {
            isTextPresent("//*[@id=\"OperatingCentres\"]/fieldset[3]/div[1]/div/p");
        });
        When("^a selfserve user creates a variation and increases the vehicle authority count$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
            clickByLinkText(world.applicationDetails.getLicenceNumber());
            world.UIJourney.changeVehicleReq(String.valueOf(world.createApplication.getNoOfVehiclesRequested() +2));
            world.UIJourney.changeVehicleAuth(String.valueOf(world.createApplication.getNoOfVehiclesRequested() + 2));
            world.UIJourney.updateFinancialInformation();
            world.UIJourney.signDeclarationForVariation();
        });
        And("^a selfserve user creates a variation and adds an operating centre$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
            world.UIJourney.changeLicenceForVariation();
            world.UIJourney.addNewOperatingCentreSelfServe(7,7);
            world.UIJourney.updateFinancialInformation();
            world.UIJourney.signDeclarationForVariation();
        });
        Then("^the \"([^\"]*)\" fee should be paid$", (String feeName) -> {
            clickByLinkText("Fees");
            selectValueFromDropDown("//*[@id='status']",SelectorType.XPATH,"All");
            waitForTextToBePresent("Grant Fee for application");
            assertEquals(getText("//table//tr[td//text()[contains(., 'Variation Fee for application')]]//*[contains(@class,'status')]",SelectorType.XPATH),"PAID");
        });
    }
}