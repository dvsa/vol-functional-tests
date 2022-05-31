package org.dvsa.testing.framework.stepdefs.vol;

import Injectors.World;
import cucumber.api.java.en.*;
import org.dvsa.testing.framework.enums.SelfServeSection;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.junit.Assert;
import Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;

public class Trailers extends BasePage {
    public World world;
    public Trailers(World world) {
        this.world = world;
    }

    @Given("I add a valid trailer number {string} and longer semi trailer is set to {string}  on the licence")
    public void addATrailer(String trailerNumber, String isLongerSemiTrailer) {
        world.selfServeNavigation.navigateToPage("Licence", SelfServeSection.TRAILERS);

    }

    @When("the trailer is successfully added to the trailer table")
    public void trailerAddedCheck() {
        world.selfServeNavigation.navigateToPage("Licence", SelfServeSection.TRAILERS);

    }

}
