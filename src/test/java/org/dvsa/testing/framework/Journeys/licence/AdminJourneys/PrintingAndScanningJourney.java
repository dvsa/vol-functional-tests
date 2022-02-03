package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;
import Injectors.World;
import activesupport.faker.FakerUtils;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

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
        waitForPageLoad();
        selectValueFromDropDown("category", SelectorType.ID, "Compliance");
        waitAndClick("subCategory", SelectorType.ID);
        selectValueFromDropDown("subCategory", SelectorType.ID, "Conviction");
        selectValueFromDropDownByIndex("description",SelectorType.ID, 0);
        enterText("entity_identifier", SelectorType.ID, Integer.toString(world.updateLicence.getCaseId()));
        waitAndClick("form-actions[submit]", SelectorType.ID);
    }

    public void addPrinter() {
        generatePostCodeAndUniqueId();
        waitAndClick("add", SelectorType.ID);
        waitAndEnterText("printer-details[printerName]", SelectorType.ID, uniqueId);
        waitAndEnterText("printer-details[description]", SelectorType.ID, postCode);
        waitAndClick("form-actions[submit]", SelectorType.ID);
        waitForElementToBePresent(createdRecord);
        ClickPage50AndWait();
        cycleThroughPaginationUntilElementIsDisplayed(world.printingAndScanningJourney.getUniqueId());
    }

    public void editPrinter() {
        generatePostCodeAndUniqueId();
        selectRandomCheckBoxOrRadioBtn("checkbox");
        waitAndClick("edit", SelectorType.ID);
        waitForTextToBePresent("Edit printer");
        replaceText("printer-details[printerName]", SelectorType.ID, uniqueId);
        replaceText("printer-details[description]", SelectorType.ID, postCode);
        waitAndClick("form-actions[submit]", SelectorType.ID);
        waitForElementToBePresent(updatedRecord);
        ClickPage50AndWait();
        cycleThroughPaginationUntilElementIsDisplayed(world.printingAndScanningJourney.getUniqueId());
    }

    public void deletePrinter() {
        selectRandomCheckBoxOrRadioBtn("checkbox");
        waitAndClick("delete", SelectorType.ID);
        waitAndClick("form-actions[confirm]", SelectorType.ID);
    }
}