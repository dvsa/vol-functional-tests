package org.dvsa.testing.framework.Journeys.licence;

import activesupport.IllegalBrowserException;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.string.Str;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.dvsa.testing.framework.stepdefs.vol.SubmitSelfServeApplication;
import scanner.AXEScanner;

import java.io.IOException;

public class FinancialHistoryJourney extends BasePage {
    private World world;

    public FinancialHistoryJourney(World world) {
        this.world = world;
    }

    public void answerNoToAllQuestionsAndSubmit(String applicationType, boolean scanOrNot) throws IllegalBrowserException, IOException {
        waitForTitleToBePresent("Financial history");
        clickById("data[financialHistoryConfirmation][insolvencyConfirmation]");
        findSelectAllRadioButtonsByValue("N");
        if (applicationType.equals("application")) {
            UIJourney.clickSaveAndContinue();
        } else if (applicationType.equals("variation")) {
            UIJourney.clickSaveAndReturn();
        }
        if (scanOrNot) {
            AXEScanner axeScanner = SubmitSelfServeApplication.scanner;
            axeScanner.scan(true);
        }
    }

    public void answerYesToAllQuestionsAndSubmit(){
        waitForTitleToBePresent("Financial history");
        clickById("data[financialHistoryConfirmation][insolvencyConfirmation]");
        findSelectAllRadioButtonsByValue("Y");
        waitAndEnterText("data[insolvencyDetails]",SelectorType.ID,Str.randomWord(155));
        UIJourney.clickSaveAndContinue();
    }
}
