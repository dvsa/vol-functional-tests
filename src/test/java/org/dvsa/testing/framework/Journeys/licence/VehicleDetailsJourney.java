package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.number.Int;
import activesupport.string.Str;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class VehicleDetailsJourney extends BasePage {

    private World world;
    String submitButton = "form-actions[submit]";
    public VehicleDetailsJourney(World world){
        this.world = world;
    }

    public void addAVehicle(boolean choice) {
        waitForTitleToBePresent("Vehicle details");
        if (choice) {
            clickById("add");
            waitForTitleToBePresent("Add vehicle");
            String num = String.valueOf(Int.random(10,99));
            String letter = String.valueOf(Str.randomLetter());
            waitAndEnterText("vrm", SelectorType.ID, String.format("P%sCUX",num));
            waitAndEnterText("plated_weight", SelectorType.ID, "5000");
            waitAndClick(submitButton, SelectorType.ID);
            if (isElementPresent("//*[@id='licence-vehicle[confirm-add]']",SelectorType.XPATH))
            {
                waitAndClick("//*[@id='licence-vehicle[confirm-add]']",SelectorType.XPATH);
                waitAndClick(submitButton, SelectorType.ID);
            }
            waitForTitleToBePresent("Vehicle details");
        } else {
            refreshPage();
            waitAndClick("//*[contains(text(),'No')]", SelectorType.XPATH);
        }
        waitAndClick("form-actions[saveAndContinue]", SelectorType.ID);
    }
}