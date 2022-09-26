package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import apiCalls.enums.OperatorType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class SafetyComplianceJourney extends BasePage {

    private World world;
    public SafetyComplianceJourney(World world){
        this.world = world;
    }

    public void addSafetyAndComplianceData() {
        waitForTitleToBePresent("Safety and compliance");
        waitAndEnterText("licence[safetyInsVehicles]", SelectorType.NAME, "10");
        if (world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString())) {
            waitAndEnterText("licence[safetyInsTrailers]", SelectorType.NAME, "10");
        }
        waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'In-house')]", SelectorType.XPATH);
        waitAndClick("table[action]", SelectorType.NAME);
    }
}
