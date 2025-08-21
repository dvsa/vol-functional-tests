package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

public class PSVJourney extends BasePage {

    String pageTitle = "Vehicle declarations";
    String nineSeatsAndAboveButton = "//input[@value='psvvs_medium_large']";
    String nineSeatsOrMoreDeclarations = "//*[@id='nineOrMore[psvNoSmallVhlConfirmation]']";
    String limousinesYes = "//*[@id='limousinesNoveltyVehicles[psvLimousines]']";
    String limousinesYesDeclarations = "//*[@id='limousinesNoveltyVehicles[psvOnlyLimousinesConfirmation]']";
    String smallVehiclesButton = "//input[@name='psvVehicleSize[size]']";
    String smallVehiclesConditionsNo = "(//input[@name='smallVehiclesIntention[psvOperateSmallVhl]'])[2]";
    String confirmSmallVehiclesConditions = "(//input[@name='smallVehiclesIntention[psvSmallVhlConfirmation]'])[2]";
    String limousinesNo = "(//input[@name='limousinesNoveltyVehicles[psvLimousines]'])[2]";
    String limousinesNoDeclarations = "(//input[@name='limousinesNoveltyVehicles[psvNoLimousineConfirmation]'])[2]";

    World world;

    public PSVJourney(World world) {
        this.world = world;
    }

    public void completeVehicleDeclarationsPage() {
        clickByLinkText(pageTitle);
        click(nineSeatsAndAboveButton, SelectorType.XPATH);
        click(nineSeatsOrMoreDeclarations, SelectorType.XPATH);
        click(limousinesYes, SelectorType.XPATH);
        click(limousinesYesDeclarations, SelectorType.XPATH);
        UniversalActions.clickSaveAndReturn();
    }

    public void selectVehicleSizeSection() {
        waitAndClick("//*[@id=\"overview-item__vehicles_size\"]/span", SelectorType.XPATH);
    }

    public void completeSmallVehicleConditionsPage() {
        UniversalActions.clickSaveAndContinue();
        waitForTextToBePresent("Select an option for: \"Confirm that you agree to the above conditions and undertakings being specified on your licence (if granted)\"");
        click("//*[@id=\"psvSmallVhlConfirmation\"]", SelectorType.XPATH);
        UniversalActions.clickSaveAndContinue();
    }

    public void completeDocumentaryEvidenceSmallVehiclesPage() {
        UniversalActions.clickSaveAndContinue();
        waitForTextToBePresent("Upload your evidence");
        findSelectAllRadioButtonsByValue("2");
        UniversalActions.clickSaveAndContinue();
    }

    public void completeLimousinesSmallVehiclesJourney() {
        UniversalActions.clickSaveAndReturn();
        waitForTextToBePresent("Select an option for: \"Are the vehicles you are applying for to be used as limousines or novelty type vehicles?\"");
        findSelectAllRadioButtonsByValue("Y");
        UniversalActions.clickSaveAndReturn();
    }

    public boolean vehicleSizeStatus(String status) {
        String sectionStatus = getText("//*[@id=\"overview-item__vehicles_size\"]", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean smallVehiclesConditionsStatus(String status) {
        String sectionStatus = getText("//*[@id=\"overview-item__psv_small_conditions\"]", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean documentaryEvidenceSmallVehiclesStatus(String status) {
        String sectionStatus = getText("//*[@id=\"overview-item__psv_documentary_evidence_small\"]", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean limousinesStatus(String status) {
        String sectionStatus = getText("//*[@id=\"overview-item__psv_operate_novelty\"]", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public void completeVehiclesWith9SeatsOrMorePage() {
        UniversalActions.clickSaveAndContinue();
        waitForTextToBePresent("Select an option for: \"Please confirm that vehicles with eight passenger seats or less will not be operated under the licence without the prior written agreement of the Traffic Commissioner who may require you to agree to certain undertakings\"");
        click("//*[@id=\"psvNoSmallVhlConfirmation\"]", SelectorType.XPATH);
        UniversalActions.clickSaveAndContinue();
    }

    public void completeLimousinesVehicles(String limousines) {
        UniversalActions.clickSaveAndReturn();
        waitForTextToBePresent("Select an option for: \"Are the vehicles you are applying for to be used as limousines or novelty type vehicles?\"");
        if (limousines.equals("Yes")) {
            findSelectAllRadioButtonsByValue("Y");
            UniversalActions.clickSaveAndReturn();
            waitForTextToBePresent("Check box to continue");
            click("//*[@id=\"lva-vehicles-declarations-novelty\"]/fieldset[1]/div[4]/div/label", SelectorType.XPATH);
        } else if (limousines.equals("No")) {
            findSelectAllRadioButtonsByValue("N");
            UniversalActions.clickSaveAndReturn();
            waitForTextToBePresent("Check box to continue");
            click("//*[@id=\"limousinesNoveltyVehicles[psvNoLimousineConfirmation]\"]", SelectorType.XPATH);
        }
        UniversalActions.clickSaveAndReturn();
    }

    public boolean vehicles9SeatsOrMoreStatus(String status) {
        String sectionStatus = getText("//*[@id=\"overview-item__psv_operate_large\"]", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public void selectVehicleSize(String vehicleSize) {
        UniversalActions.clickSaveAndContinue();
        waitForTextToBePresent("Which sizes of vehicles do you intend to operate?: value is required");
        switch (vehicleSize) {
            case "small" -> findSelectAllRadioButtonsByValue("psvvs_small");
            case "nine_and_above" -> findSelectAllRadioButtonsByValue("psvvs_medium_large");
            case "both" -> findSelectAllRadioButtonsByValue("psvvs_both");
        }
        UniversalActions.clickSaveAndContinue();
    }

    public void answerOperatingSmallVehiclesQuestion(String answer) {
        UniversalActions.clickSaveAndContinue();
        waitForTextToBePresent("Select an option for: \"Do you intend to operate small vehicles as a small part of a large bus business as detailed by Section 79A of the Public Passenger Vehicles Act (as amended by Section 265 of the Transport Act 2000)?\"");
        if (answer.equals("Yes")) {
            findSelectAllRadioButtonsByValue("Y");
        } else if (answer.equals("No")) {
            findSelectAllRadioButtonsByValue("N");
        }
        UniversalActions.clickSaveAndContinue();
    }

    public boolean areSmallVehicleConditionsPresent() {
        return isElementPresent( "//*[@id=\"lva-vehicles-declarations-small-conditions\"]/div[3]/h4[1]", SelectorType.XPATH);
    }

    public void completeWrittenExplanationSmallVehiclesPage() {
        UniversalActions.clickSaveAndContinue();
        waitForTextToBePresent("Enter a value for the field: \"Provide your written explanation\"");
        waitForTextToBePresent("Enter a value for the field: \"8 passenger seats or less (small vehicles)\"");
        waitForTextToBePresent("Enter a value for the field: \"9 passenger seats or more (large vehicles)\"");
        waitAndEnterText("psvSmallVhlNotes", SelectorType.ID, world.DataGenerator.getRandomWord());
        replaceText("psvTotalVehicleSmall", SelectorType.ID, "2");
        replaceText("psvTotalVehicleLarge", SelectorType.ID, "2");
        UniversalActions.clickSaveAndContinue();
    }

    public void completeDocumentaryEvidenceMainOccupationPage() {
        UniversalActions.clickSaveAndContinue();
        waitForTextToBePresent("Upload your financial evidence");
        findSelectAllRadioButtonsByValue("2");
        UniversalActions.clickSaveAndContinue();
    }

    public void completeMainOccupationUndertakingsPage() {
        UniversalActions.clickSaveAndContinue();
        waitForTextToBePresent("Select an option for: \"I confirm that I will comply with these requirements\"");
        click("//*[@id=\"psvOccupationRecordsConfirmation\"]", SelectorType.XPATH);
        click("//*[@id=\"psvIncomeRecordsConfirmation\"]", SelectorType.XPATH);
        UniversalActions.clickSaveAndContinue();
    }

    public boolean smallVehiclesStatus(String status) {
        String sectionStatus = getText("//*[@id=\"overview-item__psv_operate_small\"]", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean writtenExplanationSmallVehiclesStatus(String status) {
        String sectionStatus = getText("//*[@id=\"overview-item__psv_small_part_written\"]", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean documentaryEvidenceMainOccupationStatus(String status) {
        String sectionStatus = getText("//*[@id=\"overview-item__psv_documentary_evidence_large\"]", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean mainOccupationUndertakingsStatus(String status) {
        String sectionStatus = getText("//*[@id=\"overview-item__psv_main_occupation_undertakings\"]", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }
}
