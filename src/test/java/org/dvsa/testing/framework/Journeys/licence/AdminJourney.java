package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.dates.Dates;
import activesupport.faker.FakerUtils;
import cucumber.api.java.eo.Se;
import cucumber.api.java8.Pa;
import org.apache.poi.ss.formula.functions.Na;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AdminJourney extends BasePage {
    private World world;
    private FakerUtils faker = new FakerUtils();
    private String description;
    private String ownerName;

    Dates date = new Dates(org.joda.time.LocalDate::new);

    public String getDescription(){
    return description;
    }

    public String getOwnerName() {return ownerName;}

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public AdminJourney(World world){
        this.world = world;
    }

    public void selectSystemTeam() {
        selectValueFromDropDown("assignedToTeam", SelectorType.ID, "System Team");
        waitAndClick("assignedToUser", SelectorType.ID);
        String ownerName = selectRandomValueFromDropDown("assignedToUser");
        setOwnerName(ownerName);
        waitAndClick("assignedToUser", SelectorType.ID);

    }

    public void findLicenseAndNavigate() {
        enterText("search", SelectorType.NAME,   world.applicationDetails.getLicenceNumber());
        waitAndClick("//input[@name='submit']", SelectorType.XPATH);
        world.internalNavigation.getLicence();
        waitAndClick("Processing", SelectorType.LINKTEXT);
        waitAndClick("(//input[@type='checkbox'])[3]", SelectorType.XPATH);

    }

    public void reassignTask() {
        findLicenseAndNavigate();
        waitAndClick("re-assign task", SelectorType.ID);
        waitAndClick("assignedToTeam", SelectorType.ID);
        selectSystemTeam();
        clickById("form-actions[submit]");
    }

    public void generateCompany() {
        description = faker.generateCompanyName();
    }

    public void editTask() {
        generateCompany();
        findLicenseAndNavigate();
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
        HashMap<String, String> currentDate = date.getDateHashMap(0, 0, 1);
        replaceDateFieldsByPartialId("details[actionDate]", currentDate);
        selectValueFromDropDown("category", SelectorType.ID, "Licensing");
        waitForElementToBeClickable("subCategory", SelectorType.ID);
        selectValueFromDropDown("subCategory", SelectorType.ID, "General Task");
        enterText("details[description]", SelectorType.ID, description);
        selectSystemTeam();
        waitAndClick("submit", SelectorType.ID);
        waitAndClick("date", SelectorType.ID);
        selectValueFromDropDown("date", SelectorType.ID,"All" );
        waitAndClick(description, SelectorType.LINKTEXT);
        waitForTextToBePresent("System Team");
    }
}