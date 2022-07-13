package org.dvsa.testing.framework.Journeys.licence;

import Injectors.World;
import activesupport.string.Str;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class FinancialHistoryJourney extends BasePage {
    private World world;

    public FinancialHistoryJourney(World world) {
        this.world = world;
    }

    public void answerNoToAllQuestionsAndSubmit() {
        waitForTitleToBePresent("Financial history");
        clickById("data[financialHistoryConfirmation][insolvencyConfirmation]");
        findSelectAllRadioButtonsByValue("N");
        UIJourney.clickSaveAndContinue();
    }

    public void answerYesToAllQuestionsAndSubmit(){
        waitForTitleToBePresent("Financial history");
        clickById("data[financialHistoryConfirmation][insolvencyConfirmation]");
        findSelectAllRadioButtonsByValue("Y");
        waitAndEnterText("data[insolvencyDetails]",SelectorType.ID,Str.randomWord(155));
        UIJourney.clickSaveAndContinue();
    }
}
