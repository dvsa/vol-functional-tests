package org.dvsa.testing.framework.stepdefs.vol;

import org.dvsa.testing.framework.Injectors.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.dvsa.testing.framework.Journeys.licence.UIJourney.refreshPageWithJavascript;

public class GoodVarDecreaseVehicle extends BasePage {
    private final World world;
    Initialisation initialisation;
    public GoodVarDecreaseVehicle (World world) {
        this.world = world;
        this.initialisation = new Initialisation(world);
    }

    @When("A selfserve user decreases the vehicle authority count")
    public void aSelfserveUserDecreasesTheVehicleAuthorityCount() {
        world.generalVariationJourney.beginOperatingCentreVariation();
        world.operatingCentreJourney.updateOperatingCentreAuthorisation(String.valueOf(world.createApplication.getNoOfAddedHgvVehicles() - 1), String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority()));
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
        world.generalVariationJourney.beginOperatingCentreVariation();
        world.operatingCentreJourney.updateOperatingCentreAuthorisation("-6", String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority()));
    }

    @When("A selfserve user decreases the vehicle authority by invalid charecters")
    public void aSelfserveUserDecreasesTheVehicleAuthorityByInvalidCharecters() {
        world.generalVariationJourney.beginOperatingCentreVariation();
        String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority("-6", null, currentTrailerTotalAuthority);
    }
}
