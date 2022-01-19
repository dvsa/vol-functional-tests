package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;

import Injectors.World;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class TaskAllocationRulesJourney extends BasePage {
    private final World world;

    public TaskAllocationRulesJourney(World world) {this.world = world;}

    public void addTaskAllocationRule() {
        waitAndClick("add", SelectorType.ID);
        selectValueFromDropDown("category", SelectorType.ID, "Application");
        waitForElementToBeClickable("//input[@class='govuk-radios__input']", SelectorType.XPATH);
        waitAndClick("(//input[@type='radio'])[3]", SelectorType.XPATH);
        selectRandomValueFromDropDown("trafficArea");
       // selectDropDownValues();
    }
}



