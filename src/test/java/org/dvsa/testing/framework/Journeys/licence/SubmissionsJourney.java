package org.dvsa.testing.framework.Journeys.licence;

import org.dvsa.testing.framework.Injectors.World;
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
        selectRandomValueFromDropDown("user", SelectorType.ID);
        String operatorForename = world.DataGenerator.getOperatorForeName();
        waitAndEnterText("presidingTcDetails[name]", SelectorType.ID, operatorForename);
        world.UIJourney.clickSubmit();
    }

    public void createAndSubmitSubmission() {
        waitAndClick("//*[@id='menu-licence/cases']", SelectorType.XPATH);
        clickByLinkText(Integer.toString(world.updateLicence.getCaseId()));
        clickByLinkText("Submissions");
        waitAndClick("add", SelectorType.ID);
        selectValueFromDropDownByIndex("fields[submissionSections][submissionType]", SelectorType.NAME, 13);
        world.UIJourney.clickSubmit();
    }

    public void setInfoCompleteAndAssignSubmission() {
        clickByLinkText("Set info complete");
        world.UIJourney.clickSubmit();
        waitAndClick("Assign submission", SelectorType.LINKTEXT);
        waitForTextToBePresent("Assign to:");
        clickByXPath("//*[@id='tcOrOther']");
        clickByXPath("//*[@id='presidingTcUser_chosen']");
        waitAndEnterText("//*[@id='presidingTcUser_chosen']/div/div/input", SelectorType.XPATH, getSelectedValue());
        waitAndClick("//*[@id='presidingTcUser_chosen']/div/ul/li[2]", SelectorType.XPATH);
    }

    public void editSubmission() {
        clickByLinkText("Edit submission");
        selectRandomValueFromDropDown("fields[submissionSections][submissionType]", SelectorType.NAME);
        refreshPage();
        world.UIJourney.clickSubmit();
    }

    public void addTransportManagerComments() {
        clickByXPath("//form[@name='transport-managers-section-attachments']/following-sibling::a[1]");
        waitAndEnterText("fields[comment]_ifr", SelectorType.ID, "-" + world.DataGenerator.getRandomWord());
        world.UIJourney.clickSubmit();
    }

    public void closeSubmission() {
        clickByLinkText("Close submission");
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
            ((RemoteWebElement)addFile).setFileDetector(new LocalFileDetector());
            addFile.sendKeys(workingDir.concat(financialEvidenceFile));
    }
    }
}


