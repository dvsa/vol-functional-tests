package org.dvsa.testing.framework.stepdefs;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.lib.pages.BasePage;
import org.dvsa.testing.lib.pages.enums.SelectorType;

import static org.junit.Assert.assertEquals;


public class GoodVarIncreaseVehicle extends BasePage implements En  {
    World world = new World();

    public GoodVarIncreaseVehicle(World world) {

        When("^i increase my vehicle authority count$", () -> {
         world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
         clickByLinkText(world.createLicence.getLicenceNumber());
         world.UIJourneySteps.changeVehicleReq(String.valueOf(world.createLicence.getNoOfVehiclesRequired() + 1 ));
         world.UIJourneySteps.changeVehicleAuth(String.valueOf(world.createLicence.getNoOfVehiclesRequired() + 1 ));
        });

        Then("^a status of update required should be shown next to financial evidence$", () -> {
        untilExpectedTextInElement("//*[@id=\"overview-item__financial_evidence\"]",  SelectorType.XPATH,"REQUIRES ATTENTION", 10);
        });

        When("^A selfserve user increases the vehicle required count by invalid characters$", () -> {
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq("+6");
        });
        Then("^An error message should appear$", () -> {
            isTextPresent("//*[@id=\"OperatingCentre\"]/fieldset[2]/div[1]/div/p",30);
        });

        When("^A selfserve user increases the vehicle authority by invalid charecters$", () -> {
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(),world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq(String.valueOf(world.createLicence.getNoOfVehiclesRequired()));
            world.UIJourneySteps.changeVehicleAuth("+6");
        });
        Then("^An error should appear$", () -> {
            isTextPresent("//*[@id=\"OperatingCentres\"]/fieldset[3]/div[1]/div/p",30);
        });
        When("^a selfserve user creates a variation and increases the vehicle authority count$", () -> {
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            clickByLinkText(world.createLicence.getLicenceNumber());
            world.UIJourneySteps.changeVehicleReq(String.valueOf(world.createLicence.getNoOfVehiclesRequired() +2));
            world.UIJourneySteps.changeVehicleAuth(String.valueOf(world.createLicence.getNoOfVehiclesRequired() + 2));
            world.UIJourneySteps.updateFinancialInformation();
            world.UIJourneySteps.signDeclarationForVariation();
        });
        And("^a selfserve user creates a variation and adds an operating centre$", () -> {
            world.selfServeNavigation.navigateToLogin(world.createLicence.getLoginId(), world.createLicence.getEmailAddress());
            world.selfServeNavigation.navigateToPage("licence", "operating centres and authorisation");
            world.UIJourneySteps.changeLicenceForVariation();
            world.UIJourneySteps.addNewOperatingCentreSelfServe("B988QF",7,7);
            world.UIJourneySteps.updateFinancialInformation();
            world.UIJourneySteps.signDeclarationForVariation();
        });
        Then("^the \"([^\"]*)\" fee should be paid$", (String feeName) -> {
            clickByLinkText("Fees");
            selectValueFromDropDown("//*[@id='status']",SelectorType.XPATH,"All");
            waitForTextToBePresent("Grant Fee for application");
            assertEquals(getText("//table//tr[td//text()[contains(., 'Variation Fee for application')]]//*[contains(@class,'status')]",SelectorType.XPATH),"PAID");
        });
    }
}