package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.number.Int;
import activesupport.string.Str;
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
            String num = String.valueOf(Int.random(10,99));
            String letter = String.valueOf(Str.randomLetter());
            waitAndEnterText("vrm", SelectorType.ID, String.format("P%sCUX",num));
            waitAndEnterText("plated_weight", SelectorType.ID, "5000");
            world.UIJourney.clickSubmit();
            if (isElementPresent("//*[@id='licence-vehicle[confirm-add]']",SelectorType.XPATH))
            {
                waitAndClick("//*[@id='licence-vehicle[confirm-add]']",SelectorType.XPATH);
                world.UIJourney.clickSubmit();
            }
            waitForTitleToBePresent("Vehicle details");
            UIJourney.clickSaveAndContinue();
        } else {
            refreshPage();
            waitAndClick("//label[contains(text(),'No')]", SelectorType.XPATH);
            UIJourney.clickSaveAndReturn();
        }
    }
}