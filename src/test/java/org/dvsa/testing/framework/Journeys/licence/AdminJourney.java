package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.time.LocalDate;


public class AdminJourney extends BasePage {
    private World world;
    private FakerUtils faker = new FakerUtils();
    private String description;

    LocalDate date = LocalDate.now();

    public String getDescription(){
    return description;
    }

    public AdminJourney(World world){
        this.world = world;
    }

    public void selectSystemTeam()
    {
        selectValueFromDropDown("assignedToTeam", SelectorType.ID, "System Team");
        waitAndClick("assignedToUser", SelectorType.ID);
        selectValueFromDropDown("assignedToUser", SelectorType.ID, "ASHLEY ALEX ELGOOD");
        waitAndClick("assignedToUser", SelectorType.ID);

    }

    public void findLicenseAndNavigate()
    {
        enterText("search", SelectorType.NAME,   world.applicationDetails.getLicenceNumber());
        waitAndClick("//input[@name='submit']", SelectorType.XPATH);
        world.internalSearchJourney.searchLicense();
        waitAndClick("Processing", SelectorType.LINKTEXT);
        waitAndClick("(//input[@type='checkbox'])[3]", SelectorType.XPATH);

    }

    public void reassignTask()
    {
        findLicenseAndNavigate();
        waitAndClick("re-assign task", SelectorType.ID);
        waitAndClick("assignedToTeam", SelectorType.ID);
        selectSystemTeam();
        clickById("form-actions[submit]");
        waitForTextToBePresent("System Team (ELGOOD, ASHLEY ALEX)");
    }

    public void generateCompany()
    {
        description = faker.generateCompanyName();

    }

    public void editTask()
    {
        generateCompany();
        findLicenseAndNavigate();
        waitAndClick("edit", SelectorType.ID);
        waitForElementToBePresent("//*[@id='details[description]']");
        replaceText("details[description]", SelectorType.ID, description);
        selectSystemTeam();
        waitAndClick("submit", SelectorType.ID);
        waitForElementToBePresent("//p[@role='alert']");
    }

    public void addTask()
    {
        generateCompany();
        enterText("search", SelectorType.NAME,   world.applicationDetails.getLicenceNumber());
        waitAndClick("//input[@name='submit']", SelectorType.XPATH);
        world.internalSearchJourney.searchLicense();
        waitAndClick("Processing", SelectorType.LINKTEXT);
        waitAndClick("create task", SelectorType.ID);
        int day = date.getDayOfMonth();
        waitAndEnterText("details[actionDate][day]", SelectorType.NAME, Integer.toString(day));
        int month = date.getMonthValue();
        waitAndEnterText("details[actionDate]_month", SelectorType.ID, Integer.toString(month));
        int year = date.plusYears(1).getYear();
        waitAndEnterText("details[actionDate]_year", SelectorType.ID, Integer.toString(year));
        selectValueFromDropDown("category", SelectorType.ID, "Licensing");
        waitForElementToBeClickable("subCategory", SelectorType.ID);
        selectValueFromDropDown("subCategory", SelectorType.ID, "General Task");
        enterText("details[description]", SelectorType.ID, description);
        selectSystemTeam();
        waitAndClick("submit", SelectorType.ID);
        waitAndClick("date", SelectorType.ID);
        selectValueFromDropDown("date", SelectorType.ID,"All" );
        waitForTextToBePresent("System Team (ELGOOD, ASHLEY ALEX)");
    }
}