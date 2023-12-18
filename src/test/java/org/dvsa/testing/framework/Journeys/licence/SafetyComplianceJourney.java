package org.dvsa.testing.framework.Journeys.licence;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Injectors.World;
import apiCalls.enums.OperatorType;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.stepdefs.vol.SubmitSelfServeApplication;
import scanner.AXEScanner;

import java.io.IOException;

public class SafetyComplianceJourney extends BasePage {

    private World world;

    public SafetyComplianceJourney(World world) {
        this.world = world;
    }

    public void addSafetyAndComplianceData(boolean scanOrNot) throws IllegalBrowserException, IOException {
        waitForTitleToBePresent("Safety and compliance");
        waitAndEnterText("licence[safetyInsVehicles]", SelectorType.NAME, "10");
        if (world.createApplication.getOperatorType().equals(OperatorType.GOODS.asString())) {
            waitAndEnterText("licence[safetyInsTrailers]", SelectorType.NAME, "10");
        }
        waitAndClick("//*[contains(text(),'Yes')]", SelectorType.XPATH);
        waitAndClick("//*[contains(text(),'In-house')]", SelectorType.XPATH);
        waitAndClick("table[action]", SelectorType.NAME);
        if (scanOrNot) {
            AXEScanner axeScanner = SubmitSelfServeApplication.scanner;
            axeScanner.scan(false);
        }
    }
}
