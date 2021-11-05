package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class VehicleDetailsJourney extends BasePage {

    private World world;
    public VehicleDetailsJourney(World world){
        this.world = world;
    }

    public void addAVehicle(boolean choice) {
        waitForTitleToBePresent("Vehicle details");
        if (choice) {
            clickById("add");
            waitForTitleToBePresent("Add vehicle");
            waitAndEnterText("vrm", SelectorType.ID, "DWK412T");
            waitAndEnterText("plated_weight", SelectorType.ID, "5000");
            waitAndClick("form-actions[submit]", SelectorType.ID);
            waitForTitleToBePresent("Vehicle details");
        } else {
            waitAndClick("//*[contains(text(),'No')]", SelectorType.XPATH);
        }
        waitAndClick("form-actions[saveAndContinue]", SelectorType.ID);
    }
}