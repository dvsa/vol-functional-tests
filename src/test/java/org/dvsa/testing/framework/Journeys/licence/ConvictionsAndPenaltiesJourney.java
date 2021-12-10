package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.dates.Dates;
import activesupport.dates.LocalDateCalendar;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import activesupport.string.Str;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.HashMap;

public class ConvictionsAndPenaltiesJourney extends BasePage {

    private World world;

    public ConvictionsAndPenaltiesJourney(World world) {
        this.world = world;
    }

    public void answerNoToAllQuestionsAndSubmit() {
        waitForTitleToBePresent("Convictions and Penalties");
        findSelectAllRadioButtonsByValue("N");
        clickById("convictionsConfirmation[convictionsConfirmation]");
        waitAndClick("form-actions[saveAndContinue]", SelectorType.ID);
    }

    public void answerYesToAllQuestionsAndSubmit() {
        waitForTitleToBePresent("Convictions and Penalties");
        findSelectAllRadioButtonsByValue("Y");
        clickById("add");
        addPreviousConviction();
        waitForTitleToBePresent("Convictions and Penalties");
        waitAndClick("convictionsConfirmation[convictionsConfirmation]", SelectorType.ID);
        waitAndClick("form-actions[saveAndContinue]", SelectorType.ID);
    }

    public void addPreviousConviction() {
        FakerUtils faker = new FakerUtils();
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
        waitAndClick("form-actions[submit]", SelectorType.ID);
    }
}
