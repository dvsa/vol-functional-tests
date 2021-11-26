package org.dvsa.testing.framework.Journeys.licence;
import Injectors.World;
import activesupport.dates.Dates;
import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.HashMap;

public class AdminJourney extends BasePage {
    private World world;
    private FakerUtils faker = new FakerUtils();
    private String description;
    private String ownerName;
    private String abbreviation;


    Dates date = new Dates(org.joda.time.LocalDate::new);

    public String getDescription(){
    return description;
    }

    public String getOwnerName() {return ownerName;}

    public String getAbbreviation() { return abbreviation;}

    public void setAbbreviation(String abbreviation) {this.abbreviation = abbreviation;}

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public AdminJourney(World world){
        this.world = world;
    }

    public void clickAdminMenuBtn() {waitAndClick("//ul[@class='admin']//li", SelectorType.XPATH); }

    public void generateCompany() {
        description = faker.generateCompanyName();
    }

    public void generateAbbreviation() { abbreviation = RandomStringUtils.randomAlphabetic(2).toUpperCase();}

    public void navigateToTaskAllocationRules() {
        clickAdminMenuBtn();
        waitAndClick("Task allocation rules", SelectorType.LINKTEXT);
    }
    public void deleteTaskAllocationRule() {
        waitAndClick("(//input[@type='checkbox'])[2]", SelectorType.XPATH);
        waitAndClick("delete", SelectorType.ID);
        waitAndClick("form-actions[confirm]", SelectorType.ID);
    }

    public void editTaskAllocationRule() {
        waitAndClick("50", SelectorType.LINKTEXT);
        selectRandomRadioBtnFromDataTable();
        waitAndClick("edit", SelectorType.ID);
        if (isElementPresent("//th[text()='Assign operator tasks starting with these letters']", SelectorType.XPATH)) {
            generateAbbreviation();
            selectRandomRadioBtnFromDataTable();
            waitAndClick("editAlphasplit", SelectorType.ID);
            waitForElementToBeClickable("taskAlphaSplit[letters]", SelectorType.ID);
            replaceText("taskAlphaSplit[letters]", SelectorType.ID, abbreviation);
            waitAndClick("//button[@id='form-actions[submit]']", SelectorType.XPATH);
        } else {
            {
                selectValueFromDropDown("team", SelectorType.ID, "System Team");
                waitForElementToBeClickable("user", SelectorType.ID);
                String ownerName = selectRandomValueFromDropDown("user");
                setOwnerName(ownerName);
                waitAndClick("//button[text()='Save']", SelectorType.XPATH);
                waitAndClick("50", SelectorType.LINKTEXT);
            }
        }
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