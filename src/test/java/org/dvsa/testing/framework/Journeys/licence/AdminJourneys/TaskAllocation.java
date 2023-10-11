package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;

import org.dvsa.testing.framework.Injectors.World;
import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.HashMap;

public class TaskAllocation extends BasePage {
    private final World world;
    private FakerUtils faker = new FakerUtils();
    private String description;

    public TaskAllocation(World world) {this.world = world;}

    public void generateCompany() {
        description = faker.generateCompanyName();
    }

    public String getDescription() {return description;}

    public void findLicenceAndNavigate() {
        enterText("search", SelectorType.NAME,   world.applicationDetails.getLicenceNumber());
        waitAndClick("//input[@name='submit']", SelectorType.XPATH);
        world.internalNavigation.getLicence();
        waitAndClick("Processing", SelectorType.LINKTEXT);
        waitAndClick("(//input[@type='checkbox'])[3]", SelectorType.XPATH);
    }

    public void selectSystemTeam() {
        selectValueFromDropDown("assignedToTeam", SelectorType.ID, "System Team");
        waitAndClick("assignedToUser", SelectorType.ID);
        String ownerName = selectRandomValueFromDropDown("assignedToUser", SelectorType.ID);
        world.taskAllocationRulesJourney.setOwnerName(ownerName);
        waitAndClick("assignedToUser", SelectorType.ID);
    }

    public void reassignTask() {
        findLicenceAndNavigate();
        waitAndClick("re-assign task", SelectorType.ID);
        waitForTextToBePresent("Assigned to");
        selectSystemTeam();
        world.UIJourney.clickSubmit();
    }

    public void editTask() {
        generateCompany();
        findLicenceAndNavigate();
        waitAndClick("edit", SelectorType.ID);
        waitForElementToBePresent("//*[@id='details[description]']");
        replaceText("details[description]", SelectorType.ID, description);
        selectSystemTeam();
        waitAndClick("submit", SelectorType.ID);
        waitForElementToBePresent("//p[@role='alert']");
    }

    public void addTask() {
        generateCompany();
        enterText("search", SelectorType.NAME,   world.applicationDetails.getLicenceNumber());
        waitAndClick("//input[@name='submit']", SelectorType.XPATH);
        world.internalNavigation.getLicence();
        waitAndClick("Processing", SelectorType.LINKTEXT);
        waitAndClick("create task", SelectorType.ID);
        waitForTextToBePresent("Action date");
        HashMap<String, String> currentDate = world.publicHolidayJourney.date.getDateHashMap(0, 0, 1);
        enterDateFieldsByPartialId("details[actionDate]", currentDate);
        selectValueFromDropDown("category", SelectorType.ID, "Licensing");
        enterText("details[description]", SelectorType.ID, description);
        selectSystemTeam();
        waitForElementToBeClickable("subCategory", SelectorType.ID);
        selectValueFromDropDown("subCategory", SelectorType.ID, "General Task");
        waitAndClick("submit", SelectorType.ID);
        waitForTextToBePresent("Task(s) successfully created");
        waitAndClick("date", SelectorType.ID);
        selectValueFromDropDown("date", SelectorType.ID,"All" );
        waitAndClick(description, SelectorType.LINKTEXT);
        waitForTextToBePresent("System Team");
    }
}