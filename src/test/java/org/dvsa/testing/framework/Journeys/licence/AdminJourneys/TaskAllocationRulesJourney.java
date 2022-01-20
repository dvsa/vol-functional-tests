package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;

import Injectors.World;
import org.apache.commons.lang3.RandomStringUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class TaskAllocationRulesJourney extends BasePage {
    private final World world;
    private String ownerName;
    public String alphaSplit = "Assign operator tasks starting with these letters";
    private String abbreviation;

    public void generateAbbreviation() {abbreviation = RandomStringUtils.randomAlphabetic(2).toUpperCase();}

    public void setOwnerName(String ownerName) {this.ownerName = ownerName;}

    public String getOwnerName() {return ownerName;}

    public String getAbbreviation() {return abbreviation;}

    public TaskAllocationRulesJourney(World world) {this.world = world;}

    public void addTaskAllocationRule() {
        waitAndClick("add", SelectorType.ID);
        selectValueFromDropDown("category", SelectorType.ID, "Application");
        waitForElementToBeClickable("//input[@class='govuk-radios__input']", SelectorType.XPATH);
        waitAndClick("(//input[@type='radio'])[3]", SelectorType.XPATH);
        selectRandomValueFromDropDown("trafficArea");
        selectDropDownValues();
    }

    public void selectDropDownValues() {
        selectValueFromDropDown("team", SelectorType.ID, "System Team");
        waitForElementToBeClickable("user", SelectorType.ID);
        String ownerName = selectRandomValueFromDropDown("user");
        setOwnerName(ownerName);
        waitAndClick("form-actions[submit]", SelectorType.ID);
        waitAndClick("50", SelectorType.LINKTEXT);
    }

    public void editTaskAllocationRule() {
        waitAndClick("50", SelectorType.LINKTEXT);
        selectRandomRadioBtnFromDataTable();
        waitAndClick("edit", SelectorType.ID);
        if (isTextPresent(alphaSplit)) {
            generateAbbreviation();
            selectRandomRadioBtnFromDataTable();
            waitAndClick("editAlphasplit", SelectorType.ID);
            waitForElementToBeClickable("taskAlphaSplit[letters]", SelectorType.ID);
            replaceText("taskAlphaSplit[letters]", SelectorType.ID, abbreviation);
            waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
            waitForElementToBeClickable("addAlphaSplit", SelectorType.ID);
            waitForTextToBePresent("Alpha split updated");
        } else {
            {
                selectDropDownValues();
            }
        }
    }

    public void deleteTaskAllocationRule() {
        waitAndClick("(//input[@type='checkbox'])[2]", SelectorType.XPATH);
        waitAndClick("delete", SelectorType.ID);
        waitAndClick("form-actions[confirm]", SelectorType.ID);
    }
}