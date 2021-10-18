package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.Assert.assertEquals;


public class GoodVarIncreaseVehicle extends BasePage implements En  {
    World world = new World();

    public GoodVarIncreaseVehicle(World world) {

        When("^i increase my vehicle authority count$", () -> {
         world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
         world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
         world.UIJourney.changeLicenceForVariation();
         world.operatingCentreJourney.updateOperatingCentreAuthorisation(String.valueOf(world.createApplication.getNoOfAddedHgvVehicles() + 1 ));
         String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
         world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(String.valueOf(world.createApplication.getNoOfAddedHgvVehicles() + 1 ), null, currentTrailerTotalAuthority);
        });

        Then("^a status of update required should be shown next to financial evidence$", () -> {
            waitForElementToBePresent("//*[@id='overview-item__financial_evidence']");
        });

        When("^A selfserve user increases the vehicle required count by invalid characters$", () -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(),world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
            world.UIJourney.changeLicenceForVariation();
            world.operatingCentreJourney.updateOperatingCentreAuthorisation("+6");
        });
        Then("^An error message should appear$", () -> {
            isTextPresent("//*[@id=\"OperatingCentre\"]/fieldset[2]/div[1]/div/p");
        });
        Then("^An error should appear$", () -> {
            isTextPresent("//*[@id=\"OperatingCentres\"]/fieldset[3]/div[1]/div/p");
        });
        And("^a selfserve user creates a variation and adds an operating centre with \"([^\"]*)\" HGVs and \"([^\"]*)\" trailers$", (String vehicles, String trailers) -> {
            world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
            world.selfServeNavigation.navigateToPage("licence", "Operating centres and authorisation");
            world.UIJourney.changeLicenceForVariation();
            world.operatingCentreJourney.addNewOperatingCentre(vehicles, trailers);
            world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(vehicles, null, trailers);
            world.UIJourney.completeFinancialEvidencePage();
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