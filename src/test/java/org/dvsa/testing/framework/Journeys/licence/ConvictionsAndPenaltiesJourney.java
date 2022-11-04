package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.dates.Dates;
import activesupport.faker.FakerUtils;
import activesupport.string.Str;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.joda.time.LocalDate;

import java.util.HashMap;

public class ConvictionsAndPenaltiesJourney extends BasePage {

    private World world;
    FakerUtils faker = new FakerUtils();
    private String convictionDescription;

    public void generateConvictionDescription() {
        convictionDescription = world.createApplication.getCompanyNumber();
    }

    public String getConvictionDescription() {
        return convictionDescription;
    }

    public ConvictionsAndPenaltiesJourney(World world) {
        this.world = world;
    }

    public void answerNoToAllQuestionsAndSubmit() {
        waitForTitleToBePresent("Convictions and Penalties");
        findSelectAllRadioButtonsByValue("N");
        clickById("convictionsConfirmation[convictionsConfirmation]");
        UIJourney.clickSaveAndContinue();
    }

    public void answerYesToAllQuestionsAndSubmit() {
        waitForTitleToBePresent("Convictions and Penalties");
        findSelectAllRadioButtonsByValue("Y");
        clickById("add");
        addPreviousConviction();
        waitForTitleToBePresent("Convictions and Penalties");
        waitAndClick("convictionsConfirmation[convictionsConfirmation]", SelectorType.ID);
        UIJourney.clickSaveAndContinue();
    }

    public void addPreviousConviction() {
        HashMap<String, String> dates;
        waitForTitleToBePresent("Add previous conviction");
        selectValueFromDropDown("data[title]", SelectorType.ID, "Mr");
        waitAndEnterText("data[forename]", SelectorType.ID, faker.generateFirstName());
        waitAndEnterText("data[familyName]", SelectorType.ID, faker.generateFirstName());
        waitAndEnterText("data[categoryText]", SelectorType.ID, faker.generateNatureOfBusiness());
        waitAndEnterText("data[notes]", SelectorType.ID, Str.randomWord(30));
        waitAndEnterText("data[courtFpn]", SelectorType.ID, Str.randomWord(10));
        waitAndEnterText("data[penalty]", SelectorType.ID, Str.randomWord(10));

        dates = world.globalMethods.date.getDateHashMap(-5, 0, -20);
        enterText("data[convictionDate][day]", SelectorType.NAME, dates.get("day"));
        enterText("data[convictionDate][month]", SelectorType.NAME, dates.get("month"));
        enterText("data[convictionDate][year]", SelectorType.NAME, dates.get("year"));
        world.UIJourney.clickSubmit();
    }

    public void addConvictionToCase() {
        generateConvictionDescription();
        clickByLinkText("Convictions");
        waitAndClick("add", SelectorType.ID);
        selectValueFromDropDownByIndex("//select[@id='defendantType']", SelectorType.XPATH, 2);
        enterText("//input[@id='fields[personFirstname]']", SelectorType.XPATH, faker.generateFirstName());
        enterText("//input[@id='fields[personLastname]']", SelectorType.XPATH, faker.generateLastName());
        HashMap<String, String> birthDate = new Dates(LocalDate::new).getDateHashMap(0, 0, -20);
        enterDateFieldsByPartialId("fields[birthDate]", birthDate);
        selectValueFromDropDownByIndex("//select[@id='fields[msi]']", SelectorType.XPATH, 2);
        enterText("//textarea[@id='categoryText']", SelectorType.XPATH, convictionDescription);
        HashMap<String, String> offenceDate = new Dates(LocalDate::new).getDateHashMap(0, 0, -1);
        enterDateFieldsByPartialId("fields[offenceDate]", offenceDate);
        HashMap<String, String> convictionDate = new Dates(LocalDate::new).getDateHashMap(0, 0, -1);
        enterDateFieldsByPartialId("fields[convictionDate]", convictionDate);
        selectValueFromDropDownByIndex("//select[@id='fields[msi]']", SelectorType.XPATH, 2);
        selectValueFromDropDownByIndex("//select[@id='fields[isDeclared]']", SelectorType.XPATH, 2);
        world.UIJourney.clickSubmit();
    }

    public void addComplaint() {
        clickByLinkText("Complaints");
        waitAndClick("//button[text()='Add complaint']", SelectorType.XPATH);
        enterText("//input[@id='complainantForename']", SelectorType.XPATH, (faker.generateFirstName()));
        enterText("//input[@id='complainantFamilyName']", SelectorType.XPATH, (faker.generateLastName()));
        HashMap<String, String> complaintDate = new Dates(LocalDate::new).getDateHashMap(0, 0, -1);
        enterDateFieldsByPartialId("complaintDate", complaintDate);
        selectValueFromDropDownByIndex("//select[@id='complaintType']", SelectorType.XPATH, 1);
        selectValueFromDropDownByIndex("//select[@id='status']", SelectorType.XPATH, 1);
        world.UIJourney.clickSubmit();
    }

    public void completConditionUndertakings() {
        generateConvictionDescription();
        clickByLinkText("Conditions and undertakings");
        waitAndClick("add", SelectorType.ID);
        selectValueFromDropDownByIndex("type", SelectorType.ID, 2);
        selectValueFromDropDownByIndex("conditionCategory", SelectorType.ID, 3);
        enterText("notes", SelectorType.ID, convictionDescription);
        click("//*[@id='fields[fulfilled]']", SelectorType.XPATH);
        selectValueFromDropDownByIndex("attachedTo", SelectorType.ID, 2);
        world.UIJourney.clickSubmit();
    }

    public void addANote() {
        waitAndClick("add", SelectorType.ID);
        world.UIJourney.clickSubmit();
    }

}