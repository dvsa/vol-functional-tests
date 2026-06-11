package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PSVJourney extends BasePage {

    String pageTitle = "Vehicle declarations";
    String nineSeatsAndAboveButton = "//*[@id='psvvs_medium_large']";
    String nineSeatsOrMoreDeclarations = "//*[@id='nineOrMore[psvNoSmallVhlConfirmation]']";
    String limousinesYes = "//*[@id='limousinesNoveltyVehicles[psvLimousines]']";
    String limousinesYesDeclarations = "//*[@id='limousinesNoveltyVehicles[psvOnlyLimousinesConfirmation]']";
    String smallVehiclesButton = "//*[@id='psvVehicleSize[size]']";
    String smallVehiclesConditionsNo = "(//*[@id='smallVehiclesIntention[psvOperateSmallVhl]'])[2]";
    String confirmSmallVehiclesConditions = "(//*[@id='smallVehiclesIntention[psvSmallVhlConfirmation]'])[2]";
    String limousinesNo = "(//*[@id='limousinesNoveltyVehicles[psvLimousines]'])[2]";
    String limousinesNoDeclarations = "(//*[@id='limousinesNoveltyVehicles[psvNoLimousineConfirmation]'])[2]";

    World world;

    public PSVJourney(World world) {
        this.world = world;
    }

    public void completeVehicleDeclarationsPage() {
        waitAndClickByLinkText(pageTitle);
        click(nineSeatsAndAboveButton, SelectorType.XPATH);
        click(nineSeatsOrMoreDeclarations, SelectorType.XPATH);
        click(limousinesYes, SelectorType.XPATH);
        click(limousinesYesDeclarations, SelectorType.XPATH);
        UniversalActions.clickSaveAndReturn();
    }

    public void selectVehicleSizeSection() {
        waitAndClick("//*[@id='overview-item__vehicles_size']/span", SelectorType.XPATH);
    }

    public void completeSmallVehicleConditionsPage() {
        clickByXPath("//*[@id='psvSmallVhlConfirmation']");
        UniversalActions.clickSaveAndContinue();
    }

    public void completeDocumentaryEvidenceSmallVehiclesPage() {
        String workingDir = System.getProperty("user.dir");
        String evidenceFile = workingDir.concat("/src/test/resources/newspaperAdvert.jpeg");
        javaScriptExecutor("document.getElementById('files').style.display = 'block'; document.getElementById('files').removeAttribute('aria-hidden');");
        javaScriptExecutor("var f = document.getElementById('evidence[files][file]'); f.style.left='0'; f.style.position='relative'; f.classList.remove('js-visually-hidden');");
        if (System.getProperty("platform") == null) {
            waitAndEnterText("//*[@id='evidence[files][file]']", SelectorType.XPATH, evidenceFile);
        } else {
            WebElement addFile = getDriver().findElement(org.openqa.selenium.By.xpath("//*[@id='evidence[files][file]']"));
            ((RemoteWebElement) addFile).setFileDetector(new LocalFileDetector());
            addFile.sendKeys(evidenceFile);
        }
        waitForTextToBePresent("File name");
        UniversalActions.clickSaveAndContinue();
    }

    public void completeLimousinesSmallVehiclesJourney() {
        findSelectAllRadioButtonsByValue("Y");
        UniversalActions.clickSaveAndReturn();
    }

    public boolean vehicleSizeStatus(String status) {
        String sectionStatus = waitAndGetText("//*[@id='overview-item__vehicles_size']", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean vehicleSizeStatusWithRetry(String status) {
        final int MAX_RETRIES = 3;
        final long WAIT_MS = 2000;
        
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            String sectionStatus = waitAndGetText("//*[@id='overview-item__vehicles_size']", SelectorType.XPATH);
            if (sectionStatus.contains(status)) {
                return true;
            }
            if (attempt < MAX_RETRIES - 1) {
                try {
                    Thread.sleep(WAIT_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }

    public boolean smallVehiclesConditionsStatus(String status) {
        String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_small_conditions']", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean smallVehiclesConditionsStatusWithRetry(String status) {
        final int MAX_RETRIES = 3;
        final long WAIT_MS = 2000;
        
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_small_conditions']", SelectorType.XPATH);
            if (sectionStatus.contains(status)) {
                return true;
            }
            if (attempt < MAX_RETRIES - 1) {
                try {
                    Thread.sleep(WAIT_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }

    public boolean documentaryEvidenceSmallVehiclesStatus(String status) {
        String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_documentary_evidence_small']", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean documentaryEvidenceSmallVehiclesStatusWithRetry(String status) {
        final int MAX_RETRIES = 3;
        final long WAIT_MS = 2000;
        
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_documentary_evidence_small']", SelectorType.XPATH);
            if (sectionStatus.contains(status)) {
                return true;
            }
            if (attempt < MAX_RETRIES - 1) {
                try {
                    Thread.sleep(WAIT_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }

    public boolean limousinesStatus(String status) {
        String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_operate_novelty']", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean limousinesStatusWithRetry(String status) {
        final int MAX_RETRIES = 3;
        final long WAIT_MS = 2000;
        
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_operate_novelty']", SelectorType.XPATH);
            if (sectionStatus.contains(status)) {
                return true;
            }
            if (attempt < MAX_RETRIES - 1) {
                try {
                    Thread.sleep(WAIT_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }

    public void completeVehiclesWith9SeatsOrMorePage() {
        waitForElementToBePresent("//*[@id='psvNoSmallVhlConfirmation']");
        clickByXPath("//*[@id='psvNoSmallVhlConfirmation']");
        UniversalActions.clickSaveAndContinue();
    }

    public void completeLimousinesVehicles(String limousines) {
        waitForTitleToBePresent("Limousines and novelty vehicles");
        if (limousines.equals("Yes")) {
            findSelectAllRadioButtonsByValue("Y");
            clickByXPath("//*[@id='limousinesNoveltyVehicles[psvOnlyLimousinesConfirmation]']");
        } else if (limousines.equals("No")) {
            findSelectAllRadioButtonsByValue("N");
           clickByXPath("//*[@id='limousinesNoveltyVehicles[psvNoLimousineConfirmation]']");
        }
        UniversalActions.clickSaveAndReturn();
    }

    public boolean vehicles9SeatsOrMoreStatus(String status) {
        String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_operate_large']", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean vehicles9SeatsOrMoreStatusWithRetry(String status) {
        final int MAX_RETRIES = 3;
        final long WAIT_MS = 2000;
        
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_operate_large']", SelectorType.XPATH);
            if (sectionStatus.contains(status)) {
                return true;
            }
            if (attempt < MAX_RETRIES - 1) {
                try {
                    Thread.sleep(WAIT_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }

    public void selectVehicleSize(String vehicleSize) {
        switch (vehicleSize) {
            case "small" -> findSelectAllRadioButtonsByValue("psvvs_small");
            case "nine_and_above" -> findSelectAllRadioButtonsByValue("psvvs_medium_large");
            case "both" -> findSelectAllRadioButtonsByValue("psvvs_both");
        }
        UniversalActions.clickSaveAndContinue();
    }

    public void answerOperatingSmallVehiclesQuestion(String answer) {
        if (answer.equals("Yes")) {
            findSelectAllRadioButtonsByValue("Y");
        } else if (answer.equals("No")) {
            findSelectAllRadioButtonsByValue("N");
        }
        UniversalActions.clickSaveAndContinue();
    }

    public boolean areSmallVehicleConditionsPresent() {
        return isElementPresent("//*[@id='lva-vehicles-declarations-small-conditions']/div[3]/h4[1]", SelectorType.XPATH);
    }

    public void completeWrittenExplanationSmallVehiclesPage() {
        waitAndEnterText("psvSmallVhlNotes", SelectorType.ID, world.DataGenerator.getRandomWord());
        replaceText("psvTotalVehicleSmall", SelectorType.ID, "2");
        replaceText("psvTotalVehicleLarge", SelectorType.ID, "2");
        UniversalActions.clickSaveAndContinue();
    }

    public void completeDocumentaryEvidenceMainOccupationPage() {
        String workingDir = System.getProperty("user.dir");
        String evidenceFile = workingDir.concat("/src/test/resources/newspaperAdvert.jpeg");
        javaScriptExecutor("document.getElementById('files').style.display = 'block'; document.getElementById('files').removeAttribute('aria-hidden');");
        javaScriptExecutor("var f = document.getElementById('evidence[files][file]'); f.style.left='0'; f.style.position='relative'; f.classList.remove('js-visually-hidden');");
        if (System.getProperty("platform") == null) {
            waitAndEnterText("//*[@id='evidence[files][file]']", SelectorType.XPATH, evidenceFile);
        } else {
            WebElement addFile = getDriver().findElement(org.openqa.selenium.By.xpath("//*[@id='evidence[files][file]']"));
            ((RemoteWebElement) addFile).setFileDetector(new LocalFileDetector());
            addFile.sendKeys(evidenceFile);
        }
        waitForTextToBePresent("File name");
        UniversalActions.clickSaveAndContinue();
    }

    public void completeMainOccupationUndertakingsPage() {
        clickByXPath("//*[@id='psvOccupationRecordsConfirmation']");
        clickByXPath("//*[@id='psvIncomeRecordsConfirmation']");
        UniversalActions.clickSaveAndContinue();
    }

    public boolean smallVehiclesStatus(String status) {
        String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_operate_small']", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean smallVehiclesStatusWithRetry(String status) {
        final int MAX_RETRIES = 3;
        final long WAIT_MS = 2000;
        
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_operate_small']", SelectorType.XPATH);
            if (sectionStatus.contains(status)) {
                return true;
            }
            if (attempt < MAX_RETRIES - 1) {
                try {
                    Thread.sleep(WAIT_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }

    public boolean writtenExplanationSmallVehiclesStatus(String status) {
        String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_small_part_written']", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean writtenExplanationSmallVehiclesStatusWithRetry(String status) {
        final int MAX_RETRIES = 3;
        final long WAIT_MS = 2000;
        
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_small_part_written']", SelectorType.XPATH);
            if (sectionStatus.contains(status)) {
                return true;
            }
            if (attempt < MAX_RETRIES - 1) {
                try {
                    Thread.sleep(WAIT_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }

    public boolean documentaryEvidenceMainOccupationStatus(String status) {
        String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_documentary_evidence_large']", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean documentaryEvidenceMainOccupationStatusWithRetry(String status) {
        final int MAX_RETRIES = 3;
        final long WAIT_MS = 2000;
        
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_documentary_evidence_large']", SelectorType.XPATH);
            if (sectionStatus.contains(status)) {
                return true;
            }
            if (attempt < MAX_RETRIES - 1) {
                try {
                    Thread.sleep(WAIT_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }

    public boolean mainOccupationUndertakingsStatus(String status) {
        String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_main_occupation_undertakings']", SelectorType.XPATH);
        return sectionStatus.contains(status);
    }

    public boolean mainOccupationUndertakingsStatusWithRetry(String status) {
        final int MAX_RETRIES = 3;
        final long WAIT_MS = 2000;
        
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            String sectionStatus = waitAndGetText("//*[@id='overview-item__psv_main_occupation_undertakings']", SelectorType.XPATH);
            if (sectionStatus.contains(status)) {
                return true;
            }
            if (attempt < MAX_RETRIES - 1) {
                try {
                    Thread.sleep(WAIT_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }
}
