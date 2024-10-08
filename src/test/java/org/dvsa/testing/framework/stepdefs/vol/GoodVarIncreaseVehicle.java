package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoodVarIncreaseVehicle extends BasePage {
    private final World world;

    public GoodVarIncreaseVehicle (World world) {this.world = world;}

    @Then("An error message should appear")
    public void anErrorMessageShouldAppear() {
        isTextPresent("//*[@id=\"OperatingCentres\"]/fieldset[3]/div[1]/div/p");
    }

    @When("i increase my vehicle authority count")
    public void iIncreaseMyVehicleAuthorityCount() {
        world.generalVariationJourney.beginOperatingCentreVariation();
        world.operatingCentreJourney.updateOperatingCentreAuthorisation(String.valueOf(world.createApplication.getNoOfAddedHgvVehicles() + 1 ), String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority()));
        String currentTrailerTotalAuthority = String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority());
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(String.valueOf(world.createApplication.getNoOfAddedHgvVehicles() + 1 ), null, currentTrailerTotalAuthority);
    }

    @Then("a status of update required should be shown next to financial evidence")
    public void aStatusOfUpdateRequiredShouldBeShownNextToFinancialEvidence() {
        waitForElementToBePresent("//*[@id='overview-item__financial_evidence']");
    }

    @When("A selfserve user increases the vehicle required count by invalid characters")
    public void aSelfserveUserIncreasesTheVehicleRequiredCountByInvalidCharacters() {
        world.generalVariationJourney.beginOperatingCentreVariation();
        world.operatingCentreJourney.updateOperatingCentreAuthorisation("+6", String.valueOf(world.createApplication.getTotalOperatingCentreTrailerAuthority()));
    }

    @Then("An error should appear")
    public void anErrorShouldAppear() {
        isTextPresent("//*[@id=\"OperatingCentre\"]/fieldset[2]/div[1]/div/p");
    }

    @And("a selfserve user creates a variation and adds an operating centre with {string} HGVs and {string} trailers")
    public void aSelfserveUserCreatesAVariationAndIncreasesTheVehicleAuthorityCount(String vehicles, String trailers) {
        world.generalVariationJourney.beginOperatingCentreVariation();
        world.operatingCentreJourney.addNewOperatingCentre(vehicles, trailers);
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(vehicles, null, trailers);
        world.selfServeUIJourney.completeFinancialEvidencePage();
        world.selfServeUIJourney.signDeclarationForVariation();
    }

    @Then("the {string} fee should be paid")
    public void theFeeShouldBePaid(String arg0) {
        clickByLinkText("Fees");
        selectValueFromDropDown("//*[@id='status']", SelectorType.XPATH,"All");
        waitForTextToBePresent("Grant Fee for application");
        assertTrue(getElementValueByText("//table//tbody[tr//*[contains(text(),'Variation Fee for application')]]//strong[contains(@class,'govuk-tag govuk-tag--green')]", SelectorType.XPATH).equalsIgnoreCase("PAID"));
    }

    @And("a selfserve user creates a variation and adds an operating centre")
    public void aSelfserveUserCreatesAVariationAndAddsAnOperatingCentre(String vehicles, String trailers) {
        world.generalVariationJourney.beginOperatingCentreVariation();
        world.operatingCentreJourney.addNewOperatingCentre(vehicles, trailers);
        world.operatingCentreJourney.updateOperatingCentreTotalVehicleAuthority(vehicles, null, trailers);
        world.selfServeUIJourney.completeFinancialEvidencePage();
        world.selfServeUIJourney.signDeclarationForVariation();
    }
}
