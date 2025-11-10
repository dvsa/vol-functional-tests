package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;

import org.dvsa.testing.framework.Injectors.World;
import org.apache.commons.lang3.RandomStringUtils;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class TaskAllocationRulesJourney extends BasePage {
    private final World world;
    private String ownerName;
    public String alphaSplit = "Assign operator tasks starting with these letters";
    private String abbreviation;

    public void generateAbbreviation() {
        abbreviation = RandomStringUtils.randomAlphabetic(2).toUpperCase();
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public TaskAllocationRulesJourney(World world) {
        this.world = world;
    }

    public void addTaskAllocationRule() {
        waitAndClick("add", SelectorType.ID);
        selectValueFromDropDown("category", SelectorType.ID, "Application");
        waitForElementToBeClickable("//input[@class='govuk-radios__input']", SelectorType.XPATH);
        waitAndClick("(//input[@type='radio'])[3]", SelectorType.XPATH);
        selectRandomValueFromDropDown("trafficArea", SelectorType.ID);
        selectDropDownValues();
    }

    public void selectDropDownValues() {
        selectValueFromDropDownByIndex("details[team]", SelectorType.NAME, 1);
        waitForElementToBeClickable("details[user]", SelectorType.NAME);
        waitAndSelectByIndex("details[user]", SelectorType.NAME, 2);
        setOwnerName(getSelectedTextFromDropDown("//select[@name='details[user]']", SelectorType.XPATH));
        UniversalActions.clickSubmit();
        waitAndClick("50", SelectorType.LINKTEXT);
    }

    public void editTaskAllocationRule() {
        waitForTitleToBePresent("Task allocation rules");
        waitAndClick("50", SelectorType.LINKTEXT);
        selectRandomCheckBoxOrRadioBtn("checkbox");
        waitAndClick("edit", SelectorType.ID);
        if (isTextPresent(alphaSplit)) {
            generateAbbreviation();
            selectRandomRadioBtn();
            waitAndClick("editAlphasplit", SelectorType.ID);
            waitForTextToBePresent("Edit alpha split");
            waitForElementToBeClickable("taskAlphaSplit[letters]", SelectorType.ID);
            replaceText("taskAlphaSplit[letters]", SelectorType.ID, abbreviation);
            UniversalActions.clickSubmit();
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
        UniversalActions.clickConfirm();
    }
}