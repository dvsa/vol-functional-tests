package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.faker.FakerUtils;
import activesupport.number.Int;
import activesupport.string.Str;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class LicenceDetailsJourney extends BasePage {
    private World world;

    public LicenceDetailsJourney(World world) {
        this.world = world;
    }

    public void answerNoToAllQuestionsAndSubmit() {
        waitForTitleToBePresent("Licence history");
        findSelectAllRadioButtonsByValue("N");
        waitAndClick("form-actions[saveAndContinue]", SelectorType.ID);
    }
    //TODO: Licence details for every No selected
    public void answerYesToAllQuestionsAndSubmit(){
        FakerUtils faker = new FakerUtils();
        waitForTitleToBePresent("Licence history");
        findSelectAllRadioButtonsByValue("Y");
        waitAndClick("add",SelectorType.ID);
        waitForTitleToBePresent("Add licence");
        waitAndEnterText("data[licNo]",SelectorType.NAME,"OB".concat(String.valueOf(Int.random(8))));
        waitAndEnterText("data[holderName]",SelectorType.NAME,faker.generateFirstName());
        waitAndClick("//*[contains(text(),'Yes')]",SelectorType.XPATH);
        waitAndClick("form-actions[submit]", SelectorType.ID);
        waitForTitleToBePresent("Licence history");
        waitAndClick("form-actions[saveAndContinue]", SelectorType.ID);
    }
}