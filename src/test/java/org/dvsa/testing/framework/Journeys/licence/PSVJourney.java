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
    String smallVehiclesButton = "//input[@name='psvVehicleSize[size]']";
    String smallVehiclesConditionsNo = "(//input[@name='smallVehiclesIntention[psvOperateSmallVhl]'])[2]";
    String confirmSmallVehiclesConditions = "(//input[@name='smallVehiclesIntention[psvSmallVhlConfirmation]'])[2]";
    String limousinesNo = "(//input[@name='limousinesNoveltyVehicles[psvLimousines]'])[2]";
    String limousinesNoDeclarations = "(//input[@name='limousinesNoveltyVehicles[psvNoLimousineConfirmation]'])[2]";

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

    public void completeRestrictedVehicleDeclarations() {
        click(smallVehiclesButton, SelectorType.XPATH);
        click(smallVehiclesConditionsNo, SelectorType.XPATH);
        click(confirmSmallVehiclesConditions, SelectorType.XPATH);
        click(limousinesNo, SelectorType.XPATH);
        click(limousinesNoDeclarations, SelectorType.XPATH);
        UIJourney.clickSaveAndReturn();
    }
}
