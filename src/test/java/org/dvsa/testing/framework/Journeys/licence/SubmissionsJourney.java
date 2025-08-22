package org.dvsa.testing.framework.Journeys.licence;

import activesupport.aws.s3.SecretsManager;
import org.dvsa.testing.framework.Injectors.World;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;

public class SubmissionsJourney extends BasePage {

    private final World world;

    public SubmissionsJourney(World world) {
        this.world = world;
    }

    public void addPresidingTC() {
        waitAndClick("add", SelectorType.ID);
        waitAndClick("user", SelectorType.ID);
        if (!findElements("user", SelectorType.ID).get(0).getText().contains(SecretsManager.getSecretValue("presidingTC"))) {
            selectRandomValueFromDropDown("user", SelectorType.ID);
        } else {
            selectValueFromDropDown("user", SelectorType.ID, SecretsManager.getSecretValue("presidingTC"));
        }
        String operatorForename = SecretsManager.getSecretValue("presidingTC");
        waitAndEnterText("presidingTcDetails[name]", SelectorType.ID, operatorForename);
        UniversalActions.clickSubmit();
    }

    public void createAndSubmitSubmission() {
        waitAndClick("//*[@id='menu-licence/cases']", SelectorType.XPATH);
        waitAndClickByLinkText(Integer.toString(world.updateLicence.getCaseId()));
        waitAndClickByLinkText("Submissions");
        waitAndClick("add", SelectorType.ID);
        selectValueFromDropDownByIndex("fields[submissionSections][submissionType]", SelectorType.NAME, 13);
        UniversalActions.clickSubmit();
    }

    public void setInfoCompleteAndAssignSubmission() {
        waitAndClickByLinkText("Set info complete");
        UniversalActions.clickSubmit();
        waitAndClick("Assign submission", SelectorType.LINKTEXT);
        waitForTextToBePresent("Assign to:");
        clickByXPath("//*[@id='tcOrOther']");
        clickByXPath("//*[@id='presidingTcUser_chosen']");
        setSelectedValue(getText("//*[@id='presidingTcUser_chosen']/div/ul/li[2]", SelectorType.XPATH));
        waitAndEnterText("//*[@id='presidingTcUser_chosen']/div/div/input", SelectorType.XPATH, getSelectedValue());
        waitAndClick("#presidingTcUser_chosen > div > ul > li.active-result.group-option.highlighted > em", SelectorType.CSS);
    }

    public void checkTCDcDropDown() {
        waitAndClickByLinkText("Set info complete");
        UniversalActions.clickSubmit();
        waitAndClick("Assign submission", SelectorType.LINKTEXT);
        waitForElementToBePresent("//input[@name='fields[tcOrOther]']");
        clickByXPath("//input[@name='fields[tcOrOther]']");
        clickByXPath("//*[@id='presidingTcUser_chosen']");
    }

    public void editSubmission() {
        waitAndClickByLinkText("Edit submission");
        selectRandomValueFromDropDown("fields[submissionSections][submissionType]", SelectorType.NAME);
        refreshPage();
        UniversalActions.clickSubmit();
    }

    public void addTransportManagerComments() {
        clickByXPath("//form[@name='transport-managers-section-attachments']/following-sibling::a[1]");
        waitAndEnterText("fields[comment]_ifr", SelectorType.ID, "-" + world.DataGenerator.getRandomWord());
        UniversalActions.clickSubmit();
    }

    public void closeSubmission() {
        waitAndClickByLinkText("Close submission");
        clickById("form-actions[confirm]");
    }

    public void attachFile() {
        String workingDir = System.getProperty("user.dir");
        String financialEvidenceFile = "/src/test/resources/newspaperAdvert.jpeg";
        String jScript = "document.getElementById('attachments[file]').style.left = 0";
        javaScriptExecutor(jScript);

        if (System.getProperty("platform") == null) {
            enterText("//*[@id='attachments[file]']", SelectorType.XPATH, workingDir.concat(financialEvidenceFile));
        } else {
            WebElement addFile = getDriver().findElement(By.xpath("//*[@id='attachments[file]']"));
            ((RemoteWebElement) addFile).setFileDetector(new LocalFileDetector());
            addFile.sendKeys(workingDir.concat(financialEvidenceFile));
        }
    }
}


