package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;

import Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class PrintingAndScanningJourney extends BasePage {
    private final World world;

    public PrintingAndScanningJourney(World world) {this.world = world;}

    public void completeComplianceScanningDetails() {
        waitForPageLoad();
        selectValueFromDropDown("category", SelectorType.ID, "Compliance");
        waitAndClick("subCategory", SelectorType.ID);
        selectValueFromDropDown("subCategory", SelectorType.ID, "Conviction");
        selectValueFromDropDownByIndex("description",SelectorType.ID, 0);
        enterText("entity_identifier", SelectorType.ID, Integer.toString(world.updateLicence.getCaseId()));
        waitAndClick("form-actions[submit]", SelectorType.ID);
    }
}
