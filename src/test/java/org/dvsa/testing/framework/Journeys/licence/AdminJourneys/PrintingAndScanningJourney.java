package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;
import org.dvsa.testing.framework.Injectors.World;
import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.Utils.Generic.UniversalActions;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import static org.dvsa.testing.framework.Utils.Generic.UniversalActions.refreshPageWithJavascript;

public class PrintingAndScanningJourney extends BasePage {
    private final World world;
    private FakerUtils faker = new FakerUtils();
    private String uniqueId;
    private String postCode;
    public String createdRecord = "//p[text()='Created record']";
    public String updatedRecord = "//p[text()='Updated record']";

    public PrintingAndScanningJourney(World world) {this.world = world;}

    public void generateUniqueId() {uniqueId = faker.generateUniqueId(10);}

    public void generatePostCode() {postCode = world.DataGenerator.getOperatorPostCode();}

    public String getUniqueId() {return uniqueId;}

    public void generatePostCodeAndUniqueId() {
        generateUniqueId();
        generatePostCode();
    }

    public void ClickPage50AndWait(){
        scrollAndClick("50", SelectorType.LINKTEXT);
        waitForElementToBePresent("//div[@class='table__wrapper']//table/tbody[1]/tr[50]/td[1]");
    }

    public void completeComplianceScanningDetails() {
        waitForTitleToBePresent("Scanning");
        selectValueFromDropDown("category", SelectorType.ID, "Compliance");
        waitAndClick("subCategory", SelectorType.ID);
        selectValueFromDropDown("subCategory", SelectorType.ID, "Conviction");
        waitAndClick("description", SelectorType.ID);
        selectValueFromDropDown("description", SelectorType.ID, "Conviction Notification");
        enterText("entity_identifier", SelectorType.ID, Integer.toString(world.updateLicence.getCaseId()));
        UniversalActions.clickSubmit();
    }

    public void addPrinter() {
        generatePostCodeAndUniqueId();
        waitAndClick("add", SelectorType.ID);
        waitAndEnterText("printer-details[printerName]", SelectorType.ID, uniqueId);
        waitAndEnterText("printer-details[description]", SelectorType.ID, postCode);
        UniversalActions.clickSubmit();
        waitForElementToBePresent(createdRecord);
        ClickPage50AndWait();
        cycleThroughPaginationUntilElementIsDisplayed(world.printingAndScanningJourney.getUniqueId());
    }

    public void editPrinter() {
        generatePostCodeAndUniqueId();
        waitForTitleToBePresent("Printers");
        selectRandomCheckBoxOrRadioBtn("checkbox");
        waitAndClick("edit", SelectorType.ID);
        waitForTextToBePresent("Edit printer");
        replaceText("printer-details[printerName]", SelectorType.ID, uniqueId);
        replaceText("printer-details[description]", SelectorType.ID, postCode);
        UniversalActions.clickSubmit();
        waitForElementToBePresent(updatedRecord);
        ClickPage50AndWait();
        cycleThroughPaginationUntilElementIsDisplayed(world.printingAndScanningJourney.getUniqueId());
    }
    public void deletePrinter() {
        long kickOut = System.currentTimeMillis() + 120000;
        do {
            waitAndClick(String.format("//tr[td]//*[contains(text(),'%s')]//following-sibling::td",world.printingAndScanningJourney.postCode),SelectorType.XPATH);
            waitAndClick("delete", SelectorType.ID);
        } while (!isTextPresent("Remove printer") && System.currentTimeMillis() < kickOut);

        waitForTextToBePresent("Remove printer");
        UniversalActions.clickConfirm();
    }
}
