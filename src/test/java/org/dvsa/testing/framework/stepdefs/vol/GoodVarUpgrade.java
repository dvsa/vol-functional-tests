package org.dvsa.testing.framework.stepdefs.vol;

import io.cucumber.java.en.When;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class GoodVarUpgrade extends BasePage {
    private final World world;

    public GoodVarUpgrade (World world) {this.world = world;}

    @When("i upgrade my licence type to Standard National")
    public void iUpgradeMyLicenceTypeToStandardNational() {
        world.generalVariationJourney.beginUpgradeVariation();
        click(world.typeOfLicenceJourney.standardNational, SelectorType.XPATH);
        world.UIJourney.clickSaveAndReturn();
    }
}
