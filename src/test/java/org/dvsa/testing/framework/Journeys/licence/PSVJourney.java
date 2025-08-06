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

    public void completeRestrictedVehicleDeclarations() {
        click(smallVehiclesButton, SelectorType.XPATH);
        click(smallVehiclesConditionsNo, SelectorType.XPATH);
        click(confirmSmallVehiclesConditions, SelectorType.XPATH);
        click(limousinesNo, SelectorType.XPATH);
        click(limousinesNoDeclarations, SelectorType.XPATH);
        UniversalActions.clickSaveAndReturn();
    }

    public void completeVehicleSize() {
    clickByLinkText("Vehicle size");
    clickByXPath("//*[@id='psvVehicleSize[size]']");
    UniversalActions.clickSaveAndContinue();
    waitForElementToBePresent("//*[@id='psvSmallVhlConfirmation']");
    clickByXPath("//*[@id='psvSmallVhlConfirmation']");
    UniversalActions.clickSaveAndReturn();
    }


    public void selectVehicleSizeSection() {
        waitAndClick("//*[@id=\"overview-item__vehicles_size\"]/span", SelectorType.XPATH);
    }

    public void smallVehiclesLessThan9Seats() {
        UniversalActions.clickSaveAndContinue();
        waitForTextToBePresent("Which sizes of vehicles do you intend to operate?: value is required");
        findSelectAllRadioButtonsByValue("psvvs_small");
        UniversalActions.clickSaveAndContinue();
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

    public boolean isVehicleSizeUpdated() {
        String sectionStatus = getText("//*[@id=\"overview-item__vehicles_size\"]", SelectorType.XPATH);
        return sectionStatus.contains("Updated");
    }

    public boolean isSmallVehiclesConditionsUpdated() {
        String sectionStatus = getText("//*[@id=\"overview-item__psv_small_conditions\"]", SelectorType.XPATH);
        return sectionStatus.contains("Updated");
    }

    public boolean isDocumentaryEvidenceSmallVehiclesUpdated() {
        String sectionStatus = getText("//*[@id=\"overview-item__psv_documentary_evidence_small\"]", SelectorType.XPATH);
        return sectionStatus.contains("Updated");
    }

    public boolean isLimousinesUpdated() {
        String sectionStatus = getText("//*[@id=\"overview-item__psv_operate_novelty\"]", SelectorType.XPATH);
        return sectionStatus.contains("Updated");
    }
}
