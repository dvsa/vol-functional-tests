package org.dvsa.testing.framework.Journeys.page;

import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class SafetyComplianceJourney extends BasePage {

    public void addSafetyAndComplianceData() {
        waitForTitleToBePresent("Safety and compliance");
        waitAndEnterText("licence[safetyInsVehicles]", SelectorType.NAME, "10");
        waitAndEnterText("licence[safetyInsTrailers]", SelectorType.NAME, "10");
        waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'In-house')]", SelectorType.XPATH);
        waitAndClick("table[action]", SelectorType.NAME);
    }
}
