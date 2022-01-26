package org.dvsa.testing.framework.Journeys.licence.AdminJourneys;
import Injectors.World;
import activesupport.faker.FakerUtils;
import activesupport.string.Str;
import org.dvsa.testing.framework.enums.Duration;
import org.dvsa.testing.framework.pageObjects.BasePage;
import org.dvsa.testing.framework.pageObjects.enums.SelectorType;

import java.util.concurrent.TimeUnit;

public class PrintingAndScanningJourney extends BasePage {
    private final World world;
    private FakerUtils faker = new FakerUtils();
    private String uniqueId;
    private String postCode;
    public String createdRecord = "//p[text()='Created record']";
    public String updatedRecord = "//p[text()='Updated record']";
    public String removedPrinter = "//a[text()='Close']/following-sibling::p";

    public PrintingAndScanningJourney(World world) {this.world = world;}

    public void generateUniqueId() {uniqueId = faker.generateUniqueId(10);}

    public void generatePostCode() {postCode = faker.getRandomRealUKPostcode();}

    public String getUniqueId() {return uniqueId;}

    public String getRemovedPrinter() {return removedPrinter;}

    public void generatePostCodeAndUniqueId() {
        generateUniqueId();
        generatePostCode();
    }

    public void findAddedorEditedPrinter() {
        waitAndClick("50", SelectorType.LINKTEXT);
        cycleThroughPaginationUntilElementIsDisplayed(world.printingAndScanningJourney.getUniqueId());
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
        findAddedorEditedPrinter();
    }

    public void editPrinter() {
        generatePostCodeAndUniqueId();
        selectRandomCheckBoxOrRadioBtn("checkbox");
        waitAndClick("edit", SelectorType.ID);
        waitForPageLoad();
        replaceText("printer-details[printerName]", SelectorType.ID, uniqueId);
        replaceText("printer-details[description]", SelectorType.ID, postCode);
        waitAndClick("form-actions[submit]", SelectorType.ID);
        findAddedorEditedPrinter();
    }

    public void deletePrinter() {
        selectRandomCheckBoxOrRadioBtn("checkbox");
        waitAndClick("delete", SelectorType.ID);
        waitAndClick("form-actions[confirm]", SelectorType.ID);
        waitForElementToBePresent(removedPrinter);
    }
}