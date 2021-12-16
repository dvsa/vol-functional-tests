package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java8.En;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;

public class GoodVarDecreaseVehicle extends BasePage implements En {
    private final World world;

    public GoodVarDecreaseVehicle (World world) {this.world = world;}

    @When("A selfserve user decreases the vehicle authority count")
    public void aSelfserveUserDecreasesTheVehicleAuthorityCount() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.OPERATING_CENTERS_AND_AUTHORISATION);
        world.UIJourney.changeLicenceForVariation();
        world.operatingCentreJourney.updateOperatingCentreAuthorisation(String.valueOf(world.createApplication.getNoOfAddedHgvVehicles() - 1));
        String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(String.valueOf(world.createApplication.getNoOfAddedHgvVehicles() - 1), null, currentTrailerTotalAuthority);
    }

    @And("removes a vehicle because of new vehicle cap")
    public void removesAVehicleBecauseOfNewVehicleCap() {
        world.selfServeNavigation.navigateToPage("variation", SelfServeSection.VEHICLES);
        world.UIJourney.removeFirstVehicleOnVehiclePage();
        refreshPageWithJavascript();
        waitAndClick("//*[@class='govuk-back-link']", SelectorType.XPATH);
    }

    @Then("a status of update required should be shown next to Review and declarations")
    public void aStatusOfUpdateRequiredShouldBeShownNextToReviewAndDeclarations() {
        waitForElementToBePresent("//*[@id='overview-item__undertakings']");
    }

    @When("A selfserve user decreases the vehicle required count by invalid characters")
    public void aSelfserveUserDecreasesTheVehicleRequiredCountByInvalidCharacters() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.OPERATING_CENTERS_AND_AUTHORISATION);
        world.UIJourney.changeLicenceForVariation();
        world.operatingCentreJourney.updateOperatingCentreAuthorisation("-6");
    }

    @When("A selfserve user decreases the vehicle authority by invalid charecters")
    public void aSelfserveUserDecreasesTheVehicleAuthorityByInvalidCharecters() {
        world.selfServeNavigation.navigateToLogin(world.registerUser.getUserName(), world.registerUser.getEmailAddress());
        world.selfServeNavigation.navigateToPage("licence", SelfServeSection.OPERATING_CENTERS_AND_AUTHORISATION);
        world.UIJourney.changeLicenceForVariation();
        String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority("-6", null, currentTrailerTotalAuthority);
    }
}
