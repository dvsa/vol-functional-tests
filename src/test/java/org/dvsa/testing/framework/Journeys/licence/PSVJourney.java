package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class PSVJourney extends BasePage {

    String pageTitle = "Vehicle declarations";
    String nineSeatsAndAboveButton = "//input[@value='psvvs_medium_large']";
    String nineSeatsOrMoreDeclarations = "//*[@id='nineOrMore[psvNoSmallVhlConfirmation]']";
    String limousinesYes = "//*[@id='limousinesNoveltyVehicles[psvLimousines]']";
    String limousinesYesDeclarations = "//*[@id='limousinesNoveltyVehicles[psvOnlyLimousinesConfirmation]']";

    World world;

    public PSVJourney(World world) {
        this.world = world;
    }

    public void completeVehicleDeclarationsPage() {
        clickByLinkText(pageTitle);
        click(nineSeatsAndAboveButton, SelectorType.XPATH);
        click(nineSeatsOrMoreDeclarations, SelectorType.XPATH);
        click(limousinesYes, SelectorType.XPATH);
        click(limousinesYesDeclarations, SelectorType.XPATH);
        UIJourney.clickSaveAndReturn();
    }
}
