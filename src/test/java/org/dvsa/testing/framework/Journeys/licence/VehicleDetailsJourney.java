package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.number.Int;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class VehicleDetailsJourney extends BasePage {

    private World world;
    public VehicleDetailsJourney(World world){
        this.world = world;
    }

    public void addAVehicle(boolean vehicleType) {
        waitForTitleToBePresent("Vehicle details");
        if (vehicleType) {
            clickById("add");
            waitForTitleToBePresent("Add vehicle");
            String num = String.valueOf(Int.random(10,99));
            waitAndEnterText("vrm", SelectorType.ID, String.format("P%sCUX",num));
            waitAndEnterText("plated_weight", SelectorType.ID, "5000");
            world.universalActions.clickSubmit();
            if (isElementPresent("//*[@id='licence-vehicle[confirm-add]']",SelectorType.XPATH))
            {
                waitAndClick("//*[@id='licence-vehicle[confirm-add]']",SelectorType.XPATH);
                world.universalActions.clickSubmit();
            }
            waitForTitleToBePresent("Vehicle details");
            UniversalActions.clickSaveAndContinue();
        } else {
            refreshPage();
            waitAndClick("//label[contains(text(),'No')]", SelectorType.XPATH);
            UniversalActions.clickSaveAndReturn();
        }
    }
}